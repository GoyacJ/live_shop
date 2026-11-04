package vip.xiaonuo.integral.modular.rules.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import manage.UserApi;
import manage.enums.UserLimitType;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.common.enums.RefreshTimeType;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.integral.api.enums.IntegralCategory;
import vip.xiaonuo.integral.api.enums.IntegralChangeType;
import vip.xiaonuo.integral.api.enums.IntegralWithdrawalStatus;
import vip.xiaonuo.integral.modular.record.entity.IntegralRecord;
import vip.xiaonuo.integral.modular.record.service.IntegralRecordService;
import vip.xiaonuo.integral.modular.rules.entity.IntegralRules;
import vip.xiaonuo.integral.modular.rules.mapper.IntegralRulesMapper;
import vip.xiaonuo.integral.modular.rules.params.IntegralRulesAddParam;
import vip.xiaonuo.integral.modular.rules.params.IntegralRulesEditParam;
import vip.xiaonuo.integral.modular.rules.params.IntegralRulesIdParam;
import vip.xiaonuo.integral.modular.rules.params.IntegralRulesPageParam;
import vip.xiaonuo.integral.modular.rules.service.IntegralRulesService;
import vip.xiaonuo.integral.modular.withdrawal.entity.IntegralWithdrawalRecord;
import vip.xiaonuo.integral.modular.withdrawal.service.IntegralWithdrawalRecordService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 13:28
 */
@Slf4j
@Component
public class IntegralRulesServiceImpl extends ServiceImpl<IntegralRulesMapper, IntegralRules> implements IntegralRulesService {

    @Inject
    private IntegralRecordService integralRecordService;

    @Inject
    private IntegralWithdrawalRecordService integralWithdrawalRecordService;

    @Inject
    private UserApi userApi;

    @Override
    public Page<IntegralRules> page(IntegralRulesPageParam param) {
        QueryWrapper queryWrapper = new QueryWrapper();

        if (StringUtils.isNotEmpty(param.userLimitType())) {
            queryWrapper.like(IntegralRules::getUserLimitType, param.userLimitType());
        }

        if (StringUtils.isNotEmpty(param.userId())) {
            queryWrapper.like(IntegralRules::getUserId, param.userId());
        }

        if (StringUtils.isNotEmpty(param.status())) {
            queryWrapper.eq(IntegralRules::getStatus, param.status());
        }

        if (ObjectUtil.isNotEmpty(param.startCreateTime()) && ObjectUtil.isNotEmpty(param.endCreateTime())) {
            queryWrapper.between(IntegralRules::getCreateTime, param.startCreateTime(), param.endCreateTime());
        }

        // 关键词查询
        if (ObjectUtil.isNotEmpty(param.searchKey())) {
            // sql注入处理
            queryWrapper.and(wq -> {
                wq.where(wq1 -> {
                    wq1.or(IntegralRules::getUserId).like(param.searchKey())
                            .or(IntegralRules::getUserLimitType).like(param.searchKey());

                });
            });
        }

        if (ObjectUtil.isAllNotEmpty(param.sortField(), param.sortOrder())) {
            CommonSortOrderEnum.validate(param.sortOrder());
            queryWrapper.orderBy(CharSequenceUtil.toUnderlineCase(param.sortField()), param.sortOrder().equals(CommonSortOrderEnum.ASC.getValue()));
        } else {
            queryWrapper.orderBy(IntegralRules::getCreateTime, false);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public void add(IntegralRulesAddParam param) {
        this.save(param.toBean());
    }

    @Override
    public void edit(IntegralRulesEditParam param) {
        this.updateById(param.toBean());
    }

    @Override
    public IntegralRules detail(String id) {
        return this.getById(id);
    }

    @Override
    public void delete(CommonValidList<IntegralRulesIdParam> param) {
        this.removeByIds(param.getList().stream().map(IntegralRulesIdParam::id).toList());
    }

    @Override
    public void changeStatus(String id, SimpleStatus status) {
        IntegralRules integralRules = new IntegralRules();
        integralRules.setId(id);
        integralRules.setStatus(status);
        this.updateById(integralRules);
    }

    @Override
    public Map<RefreshTimeType, IntegralRules> getIntegralRulesByUserId(String userId) {
        // 获取特殊规则
        Map<RefreshTimeType, IntegralRules> specialRulesMap = this.list(new QueryWrapper()
                .eq(IntegralRules::getUserLimitType, UserLimitType.SPECIAL)
                .eq(IntegralRules::getUserId, userId)
                .eq(IntegralRules::getStatus, SimpleStatus.ENABLE.getCode())
        ).stream().collect(Collectors.toMap(
                IntegralRules::getLimitTimeType,
                Function.identity(),
                (existing, replacement) -> replacement
        ));

        if (MapUtil.isNotEmpty(specialRulesMap)) {
            return specialRulesMap;
        } else {
            // 获取用户规则
            Map<RefreshTimeType, IntegralRules> normalRulesMap = this.list(new QueryWrapper()
                    .eq(IntegralRules::getUserLimitType, UserLimitType.NORMAL)
                    .eq(IntegralRules::getStatus, SimpleStatus.ENABLE.getCode())
            ).stream().collect(Collectors.toMap(
                    IntegralRules::getLimitTimeType,
                    Function.identity(),
                    (existing, replacement) -> replacement
            ));
            if (MapUtil.isNotEmpty(normalRulesMap)) {
                return normalRulesMap;
            } else {
                // 获取通用规则
                return this.list(new QueryWrapper()
                        .eq(IntegralRules::getUserLimitType, UserLimitType.ALL)
                        .eq(IntegralRules::getStatus, SimpleStatus.ENABLE.getCode())
                ).stream().collect(Collectors.toMap(
                        IntegralRules::getLimitTimeType,
                        Function.identity(),
                        (existing, replacement) -> replacement
                ));
            }
        }
    }

    @Override
    public Map<RefreshTimeType, IntegralRules> getWithdrawalIntegralRulesByUserId(String userId) {
        // 获取特殊规则
        Map<RefreshTimeType, IntegralRules> specialRulesMap = this.list(new QueryWrapper()
                .eq(IntegralRules::getUserLimitType, UserLimitType.SPECIAL)
                .eq(IntegralRules::getUserId, userId)
                .eq(IntegralRules::getStatus, SimpleStatus.ENABLE.getCode())
        ).stream().collect(Collectors.toMap(
                IntegralRules::getWithdrawalTimeType,
                Function.identity(),
                (existing, replacement) -> replacement
        ));

        if (MapUtil.isNotEmpty(specialRulesMap)) {
            return specialRulesMap;
        } else {
            // 获取用户规则
            Map<RefreshTimeType, IntegralRules> normalRulesMap = this.list(new QueryWrapper()
                    .eq(IntegralRules::getUserLimitType, UserLimitType.NORMAL)
                    .eq(IntegralRules::getStatus, SimpleStatus.ENABLE.getCode())
            ).stream().collect(Collectors.toMap(
                    IntegralRules::getWithdrawalTimeType,
                    Function.identity(),
                    (existing, replacement) -> replacement
            ));
            if (MapUtil.isNotEmpty(normalRulesMap)) {
                return normalRulesMap;
            } else {
                // 获取通用规则
                return this.list(new QueryWrapper()
                        .eq(IntegralRules::getUserLimitType, UserLimitType.ALL)
                        .eq(IntegralRules::getStatus, SimpleStatus.ENABLE.getCode())
                ).stream().collect(Collectors.toMap(
                        IntegralRules::getWithdrawalTimeType,
                        Function.identity(),
                        (existing, replacement) -> replacement
                ));
            }
        }
    }

    @Override
    public boolean checkIncreaseRules(String userId, BigDecimal integral) {
        Map<RefreshTimeType, IntegralRules> rules = getIntegralRulesByUserId(userId);
        if (MapUtil.isEmpty(rules)) {
            return true;
        }

        // 优先检查的周期顺序
        List<RefreshTimeType> checkOrder = Arrays.asList(
                RefreshTimeType.DAY,
                RefreshTimeType.WEEK,
                RefreshTimeType.MONTH,
                RefreshTimeType.YEAR
        );

        LocalDateTime now = LocalDateTime.now();
        for (RefreshTimeType type : checkOrder) {
            IntegralRules rule = rules.get(type);
            if (rule == null) {
                continue;
            }

            LocalDateTime startTime = type.getStartTime();
            BigDecimal total = getWithdrawalIntegralTotal(startTime, now, userId);

            if (compareIntegral(total, rule.getLimitIntegral())) {
                return false;
            }
        }

        return true;
    }

    /**
     * 积分比较
     *
     * @param totalIntegral 总量
     * @param integral      积分
     * @return 总量 > 积分
     */
    private boolean compareIntegral(BigDecimal totalIntegral, BigDecimal integral) {
        return totalIntegral.compareTo(integral) > 0;
    }

    /**
     * 计算积分总量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param userId    用户Id
     * @return 积分总量
     */
    private BigDecimal getIntegralTotal(LocalDateTime startTime, LocalDateTime endTime, String userId) {
        QueryWrapper query = new QueryWrapper();
        query.eq(IntegralRecord::getUserId, userId);
        query.eq(IntegralRecord::getChangeType, IntegralChangeType.INCREASE.getCode());
        query.eq(IntegralRecord::getIntegralCategory, IntegralCategory.INTEGRAL.getCode());
        query.between(IntegralRecord::getOperationTime, startTime, endTime);
        query.select(QueryMethods.sum(IntegralRecord::getIntegral).as("total"));
        Object total = integralRecordService.getObj(query);
        return Convert.toBigDecimal(total, BigDecimal.ZERO);
    }

    /**
     * 计算积分提现总量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param userId    用户Id
     * @return 积分总量
     */
    private BigDecimal getWithdrawalIntegralTotal(LocalDateTime startTime, LocalDateTime endTime, String userId) {
        QueryWrapper query = new QueryWrapper();
        query.eq(IntegralWithdrawalRecord::getUserId, userId);
        query.ne(IntegralWithdrawalRecord::getWithdrawalStatus, IntegralWithdrawalStatus.NOT_WITHDRAWAL.getCode());
        query.between(IntegralWithdrawalRecord::getWithdrawalTime, startTime, endTime);
        query.select(QueryMethods.sum(IntegralWithdrawalRecord::getIntegral).as("total"));
        Object total = integralWithdrawalRecordService.getObj(query);
        return Convert.toBigDecimal(total, BigDecimal.ZERO);
    }

    @Override
    public boolean checkWithdrawalRules(String userId, BigDecimal integral) {
        Map<RefreshTimeType, IntegralRules> rules = getIntegralRulesByUserId(userId);
        if (MapUtil.isEmpty(rules)) {
            return true;
        }

        // 优先检查的周期顺序
        List<RefreshTimeType> checkOrder = Arrays.asList(
                RefreshTimeType.DAY,
                RefreshTimeType.WEEK,
                RefreshTimeType.MONTH,
                RefreshTimeType.YEAR
        );

        LocalDateTime now = LocalDateTime.now();
        for (RefreshTimeType type : checkOrder) {
            IntegralRules rule = rules.get(type);
            if (rule == null) {
                continue;
            }

            LocalDateTime startTime = type.getStartTime();
            BigDecimal total = getIntegralTotal(startTime, now, userId);

            if (compareIntegral(total, rule.getWithdrawalIntegral())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public BigDecimal getPointsToCashRatio(String userId) {
        Map<RefreshTimeType, IntegralRules> rules = this.getWithdrawalIntegralRulesByUserId(userId);
        if (rules.containsKey(RefreshTimeType.DAY)) {
            return rules.get(RefreshTimeType.DAY).getPointsToCashRatio();
        }
        if (rules.containsKey(RefreshTimeType.WEEK)) {
            return rules.get(RefreshTimeType.WEEK).getPointsToCashRatio();
        }
        if (rules.containsKey(RefreshTimeType.MONTH)) {
            return rules.get(RefreshTimeType.MONTH).getPointsToCashRatio();
        }
        if (rules.containsKey(RefreshTimeType.YEAR)) {
            return rules.get(RefreshTimeType.YEAR).getPointsToCashRatio();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public LocalDateTime getValidityTime(String userId) {
        Map<RefreshTimeType, IntegralRules> rules = this.getIntegralRulesByUserId(userId);
        if (rules.containsKey(RefreshTimeType.DAY)) {
            return rules.get(RefreshTimeType.DAY).getValidityPeriod();
        }
        if (rules.containsKey(RefreshTimeType.WEEK)) {
            return rules.get(RefreshTimeType.WEEK).getValidityPeriod();
        }
        if (rules.containsKey(RefreshTimeType.MONTH)) {
            return rules.get(RefreshTimeType.MONTH).getValidityPeriod();
        }
        if (rules.containsKey(RefreshTimeType.YEAR)) {
            return rules.get(RefreshTimeType.YEAR).getValidityPeriod();
        }
        return null;
    }


}
