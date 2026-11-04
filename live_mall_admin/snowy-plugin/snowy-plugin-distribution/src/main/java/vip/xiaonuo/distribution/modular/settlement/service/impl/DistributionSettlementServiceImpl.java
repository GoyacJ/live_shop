package vip.xiaonuo.distribution.modular.settlement.service.impl;

import api.order.enums.SettlementStatus;
import api.shop.OrderApi;
import api.shop.dto.OrderManagePageDTO;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import manage.dto.UserInfoDTO;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import org.noear.solon.scheduling.annotation.Scheduled;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.enums.OrderStatusEnum;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.pojo.dto.CommonPageParam;
import vip.xiaonuo.distribution.api.dto.UserOwnPromotionDTO;
import vip.xiaonuo.distribution.api.dto.UserOwnPromotionParamDTO;
import vip.xiaonuo.distribution.api.enums.*;
import vip.xiaonuo.distribution.api.exception.DistributionException;
import vip.xiaonuo.distribution.modular.info.entity.DistributionInfo;
import vip.xiaonuo.distribution.modular.info.service.DistributionInfoService;
import vip.xiaonuo.distribution.modular.record.service.DistributionRecordService;
import vip.xiaonuo.distribution.modular.settlement.entity.DistributionSettlement;
import vip.xiaonuo.distribution.modular.settlement.mapper.DistributionSettlementMapper;
import vip.xiaonuo.distribution.modular.settlement.service.DistributionSettlementService;
import vip.xiaonuo.integral.api.IntegralApi;
import vip.xiaonuo.integral.api.dto.ChangeIntegralDTO;
import vip.xiaonuo.integral.api.enums.IntegralOperationSource;
import vip.xiaonuo.integral.api.enums.IntegralSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

import static vip.xiaonuo.distribution.api.enums.DistributionType.ONCE_REGISTER;
import static vip.xiaonuo.distribution.api.enums.DistributionType.SHOPPING;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 10:59
 */
@Tran
@Slf4j
@Component
public class DistributionSettlementServiceImpl extends ServiceImpl<DistributionSettlementMapper, DistributionSettlement> implements DistributionSettlementService {

    @Inject
    private IntegralApi integralApi;

    @Inject
    private DistributionInfoService distributionInfoService;

    @Inject
    private UserApi userApi;

    @Inject
    private OrderApi orderApi;

    @Inject
    private DistributionRecordService distributionRecordService;

    @Override
    public Page<DistributionSettlement> page(CommonPageParam param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(DistributionSettlement::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }
        // 关键词查询
        if (ObjectUtil.isNotEmpty(param.searchKey())) {
            // sql注入处理
            queryWrapper.and(wq -> {
                wq.where(wq1 -> {
                    wq1.or(DistributionSettlement::getUserId).like(param.searchKey())
                            .or(DistributionSettlement::getSettlementType).like(param.searchKey());

                });
            });
        }
        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(DistributionSettlement::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public DistributionSettlement detail(String id) {
        return this.getById(id);
    }

    @Override
    public DistributionSettlement createSettlement(String userId, String businessId, String infoId, String remark) {
        return createSettlement(userId, businessId, distributionInfoService.detail(infoId), remark);
    }

    @Override
    public DistributionSettlement createSettlement(String userId, String businessId, DistributionInfo info, String remark) {
        UserInfoDTO user = userApi.getUserById(userId);
        if (Objects.isNull(user)) {
            log.error("创建结算: 用户不存在, userId: {}", userId);
            throw new DistributionException("用户不存在");
        }
        return createSettlement(userId, user.getPId(), businessId, info, remark);
    }

    @Override
    public DistributionSettlement createSettlement(String userId, String promoterId, String businessId, DistributionInfo info, String remark) {
        DistributionSettlement settlement = new DistributionSettlement();
        settlement.setId(IdUtil.getSnowflakeNextIdStr());
        settlement.setInfoId(info.getId());
        settlement.setUserId(userId);
        settlement.setType(info.getType());
        settlement.setPromoterId(promoterId);
        settlement.setRecommendCommission(info.getRecommendCommission());
        settlement.setCommissionIntegral(info.getCommissionIntegral());
        settlement.setSettlementType(info.getSettlementType());
        settlement.setSettlementTimeType(info.getSettlementTimeType());
        settlement.setCommissionTimeType(info.getCommissionTimeType());
        settlement.setCommissionFixTime(info.getCommissionFixTime());
        settlement.setBusinessId(businessId);
        settlement.setSettlementType(info.getSettlementType());

        BigDecimal integral;
        switch (settlement.getType()) {
            case ONCE_REGISTER -> {
                integral = onceRegisterSettlementIntegral(settlement.getCommissionIntegral());
                settlement.setRemark(ONCE_REGISTER.getName());
            }
            case SHOPPING -> {
                integral = shoppingSettlementIntegral(settlement.getRecommendCommission(), settlement.getCommissionIntegral(), businessId);
                settlement.setRemark(SHOPPING.getName());
            }
            default -> integral = BigDecimal.ZERO;
        }

        // 设置结算积分
        settlement.setIntegral(integral);
        // 增加冻结积分
        distributeeFrozenIntegral(settlement.getUserId(), settlement.getPromoterId(), settlement.getId(), settlement.getIntegral());
        settlement.setCommissionTimeStatus(CommissionTimeStatus.UNSETTLED);
        // 保存
        if (this.save(settlement)) {
            return settlement;
        }
        throw new DistributionException("创建结算失败");
    }

    @Override
    public void settlement(DistributionSettlement settlement) {
        SettlementType type = settlement.getSettlementType();
        SettlementTimeType timeType = settlement.getSettlementTimeType();
        DistributionType distributionType = settlement.getType();

        if (type == null) {
            throw new DistributionException("结算类型不能为空");
        }

        if (type == SettlementType.MANUAL) {
            doImmediateSettlement(settlement);
        } else if (type == SettlementType.AUTO) {
            autoSettle(settlement, timeType, distributionType);
        } else {
            throw new DistributionException("结算类型错误");
        }

        // 持久化更新结算状态和时间
        UpdateChain.of(DistributionSettlement.class)
                .eq(DistributionSettlement::getId, settlement.getId())
                .set(DistributionSettlement::getSettlementTime, settlement.getSettlementTime())
                .set(DistributionSettlement::getSettlementStatus, settlement.getSettlementStatus())
                .update();
    }

    private void autoSettle(DistributionSettlement settlement, SettlementTimeType timeType, DistributionType distributionType) {
        if (timeType == null) {
            throw new DistributionException("结算时间类型不能为空");
        }

        switch (distributionType) {
            case ONCE_REGISTER -> doImmediateSettlement(settlement);
            case SHOPPING -> {
                OrderManagePageDTO order = orderApi.orderDetail(settlement.getBusinessId());
                if (order == null) {
                    throw new DistributionException("订单不存在");
                }

                if (order.getOrderStatus() != OrderStatusEnum.COMPLETED) {
                    return; // 未完成订单不结算
                }

                if (timeType == SettlementTimeType.AUTO) {
                    doImmediateSettlement(settlement);
                } else if (timeType == SettlementTimeType.CUSTOM) {
                    settlement.setSettlementTime(order.getOrderTime().plusDays(7));
                    settlement.setSettlementStatus(SettlementStatus.WAIT_SETTLED);
                } else {
                    throw new DistributionException("结算时间类型错误");
                }
            }
            default -> throw new DistributionException("分销类型错误");
        }
    }

    private void doImmediateSettlement(DistributionSettlement settlement) {
        settlement.setSettlementTime(LocalDateTime.now());
        settlement.setSettlementStatus(SettlementStatus.SETTLED);

        switch (settlement.getCommissionTimeType()) {
            case AUTO -> {
                settlement.setCommissionTimeStatus(CommissionTimeStatus.SETTLED);
                unlockFrozenIntegral(
                        settlement.getPromoterId(),
                        settlement.getId(),
                        settlement.getIntegral());
            }
            case CUSTOM -> {
                settlement.setCommissionTimeStatus(CommissionTimeStatus.WAIT_SETTLED);
                try {
                    String commissionFixTime = settlement.getCommissionFixTime();
                    int day = Integer.parseInt(commissionFixTime);
                    LocalDateTime commissionTime = getNextTargetDay(day);
                    settlement.setCommissionTime(commissionTime);
                } catch (NumberFormatException e) {
                    throw new DistributionException("结算时间格式错误");
                }

            }
            default -> throw new DistributionException("推广时间类型错误");
        }

    }

    public LocalDateTime getNextTargetDay(int targetDay) {
        LocalDate today = LocalDate.now();
        int currentDay = today.getDayOfMonth();

        YearMonth targetMonth = currentDay <= targetDay
                ? YearMonth.from(today)
                : YearMonth.from(today).plusMonths(1);

        // 取目标月最大天数，避免越界
        int day = Math.min(targetDay, targetMonth.lengthOfMonth());

        return LocalDateTime.of(targetMonth.getYear(), targetMonth.getMonth(), day, 0, 0);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void settlementWait() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = yesterday.atTime(LocalTime.MAX);

        List<DistributionSettlement> settlements = this.list(new QueryWrapper()
                .eq(DistributionSettlement::getSettlementStatus, SettlementStatus.WAIT_SETTLED)
                .between(DistributionSettlement::getSettlementTime, startOfDay, endOfDay));

        settlements.forEach(settlement -> {
            trySettleFrozenIntegral(settlement);
            distributionRecordService.createRecord(settlement);
        });
    }

    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void commissionWait() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = yesterday.atTime(LocalTime.MAX);
        List<DistributionSettlement> settlements = this.list(new QueryWrapper()
                .eq(DistributionSettlement::getCommissionTimeStatus, CommissionTimeStatus.WAIT_SETTLED)
                .between(DistributionSettlement::getCommissionTime, startOfDay, endOfDay));
        settlements.forEach(settlement -> {
            doImmediateSettlement(settlement);
            distributionRecordService.createRecord(settlement);
        });
    }

    @Override
    public UserOwnPromotionDTO userOwnPromotion(UserOwnPromotionParamDTO param) {
        QueryWrapper queryWrapper = new QueryWrapper()
                .eq(DistributionSettlement::getUserId, param.userId());

        if (Objects.nonNull(param.startTime()) && Objects.nonNull(param.endTime())) {
            queryWrapper.between(DistributionSettlement::getCreateTime, param.startTime(), param.endTime());
        }

        Page<UserOwnPromotionDTO.UserOwnPromotionDetailDTO> detailPage = this.page(CommonPageRequest.defaultPage(), queryWrapper)
                .map(data -> {
                    if (SHOPPING.equals(data.getType())) {
                        OrderManagePageDTO order = this.orderApi.orderDetail(data.getBusinessId());
                        if (Objects.nonNull(order)) {
                            return new UserOwnPromotionDTO.UserOwnPromotionDetailDTO(
                                    data.getId(),
                                    order.getOrderNumber(),
                                    data.getType().getName(),
                                    data.getIntegral(),
                                    data.getSettlementTime(),
                                    data.getSettlementStatus().getName());
                        }
                        return null;
                    } else if (ONCE_REGISTER.equals(data.getType())) {
                        return new UserOwnPromotionDTO.UserOwnPromotionDetailDTO(
                                data.getId(),
                                null,
                                data.getType().getName(),
                                data.getIntegral(),
                                data.getSettlementTime(),
                                data.getSettlementStatus().getName());
                    }
                    return null;
                });

        // 推广积分
        QueryWrapper totalQuery = new QueryWrapper()
                .eq(DistributionSettlement::getUserId, param.userId());

        if (Objects.nonNull(param.startTime()) && Objects.nonNull(param.endTime())) {
            totalQuery.between(DistributionSettlement::getCreateTime, param.startTime(), param.endTime());
        }
        totalQuery.select(QueryMethods.sum(DistributionSettlement::getIntegral).as("total"));
        Object total = this.getObj(totalQuery);
        return new UserOwnPromotionDTO(
                Objects.isNull(total) ? BigDecimal.ZERO : Convert.toBigDecimal(total),
                detailPage
        );
    }

    /**
     * 尝试结算并解锁积分，结算后状态变更为 SETTLED
     */
    private void trySettleFrozenIntegral(DistributionSettlement settlement) {
        try {
            unlockFrozenIntegral(
                    settlement.getPromoterId(),
                    settlement.getId(),
                    settlement.getIntegral());

            // 持久化更新状态
            UpdateChain.of(DistributionSettlement.class)
                    .eq(DistributionSettlement::getId, settlement.getId())
                    .set(DistributionSettlement::getSettlementStatus, SettlementStatus.SETTLED)
                    .set(DistributionSettlement::getSettlementTime, LocalDateTime.now())
                    .update();
        } catch (Exception e) {
            log.error("结算失败，ID={}，原因：{}", settlement.getId(), e.getMessage(), e);
        }
    }

    /**
     * 冻结积分发放
     */
    private void distributeeFrozenIntegral(String userId, String promoterId, String settlementId, BigDecimal integral) {
        integralApi.changeIntegral(ChangeIntegralDTO.increaseFrozenIntegral(
                promoterId,
                integral,
                userId,
                IntegralSource.PROMOTION,
                settlementId,
                IntegralOperationSource.SYSTEM,
                "推广积分,推广用户Id: " + userId
        ));
    }

    /**
     * 冻结积分解锁
     */
    private void unlockFrozenIntegral(String promoterId, String settlementId, BigDecimal integral) {
        integralApi.changeIntegral(ChangeIntegralDTO.unlockFrozenIntegral(
                promoterId,
                integral,
                promoterId,
                IntegralSource.PROMOTION,
                settlementId,
                IntegralOperationSource.SYSTEM,
                "推广积分结算解锁"
        ));
    }

    /**
     * 首次注册积分计算
     *
     * @param commissionIntegral 返佣积分/比例
     * @return 积分
     */
    private BigDecimal onceRegisterSettlementIntegral(BigDecimal commissionIntegral) {
        return commissionIntegral;
    }

    /**
     * 购买商品积分计算
     *
     * @param recommendCommission 积分返佣方式
     * @param commissionIntegral  返佣积分/比例
     * @param orderId             订单Id
     * @return 积分
     */
    private BigDecimal shoppingSettlementIntegral(RecommendCommission recommendCommission, BigDecimal commissionIntegral, String orderId) {
        OrderManagePageDTO order = this.orderApi.orderDetail(orderId);
        if (Objects.isNull(order)) {
            log.error("购买商品积分计算: 订单不存在, orderId: {}", orderId);
            throw new DistributionException("分销:积分结算:订单不存在");
        }
        BigDecimal integral;
        switch (recommendCommission) {
            case FIXED -> integral = commissionIntegral;
            case RATIO -> {
                if (Objects.isNull(order.getOrderAmount()) || BigDecimal.ZERO.compareTo(order.getOrderAmount()) == 0) {
                    return BigDecimal.ZERO;
                }
                // 转换成BigDecimal百分数
                BigDecimal rate = commissionIntegral
                        .divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);

                integral = order.getOrderAmount().multiply(rate)
                        .setScale(0, RoundingMode.HALF_UP);
            }
            default -> throw new DistributionException("不支持的积分计算方式");
        }
        return integral;
    }
}
