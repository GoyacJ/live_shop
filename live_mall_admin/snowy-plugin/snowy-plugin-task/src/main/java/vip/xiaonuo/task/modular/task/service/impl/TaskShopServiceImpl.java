package vip.xiaonuo.task.modular.task.service.impl;

import api.shop.OrderApi;
import api.shop.dto.OrderManagePageDTO;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vip.xiaonuo.task.api.TaskException;
import vip.xiaonuo.task.modular.info.entity.TaskInfo;
import vip.xiaonuo.task.modular.task.service.TaskShopService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 22:13
 */
@Slf4j
@Component
public class TaskShopServiceImpl implements TaskShopService {

    @Inject
    private OrderApi orderApi;

    @Override
    public BigDecimal visit(String userId, String businessId, TaskInfo taskInfo) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal shopShare(String userId, String businessId, TaskInfo taskInfo) {
        return taskInfo.getRewardIntegral();
    }

    @Override
    public BigDecimal comment(String userId, String businessId, TaskInfo taskInfo) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal shopBuy(String userId, String businessId, TaskInfo taskInfo) {
        OrderManagePageDTO dto = this.orderApi.orderDetail(businessId);
        if (Objects.isNull(dto)) {
            throw new TaskException("商品信息异常");
        }

        Integer numberParms = taskInfo.getNumberParms();
        BigDecimal orderAmount = dto.getOrderAmount();
        BigDecimal rewardIntegral = taskInfo.getRewardIntegral();

        BigDecimal result = getOrderAmountResult(numberParms, orderAmount, rewardIntegral);

        // 验证计算结果有效性
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new TaskException("积分计算结果不能为负数");
        }

        return result;
    }

    private static BigDecimal getOrderAmountResult(Integer numberParms, BigDecimal orderAmount, BigDecimal rewardIntegral) {
        if (numberParms == null || numberParms <= 0) {
            throw new TaskException("消费金额基数必须大于0");
        }
        if (orderAmount == null) {
            throw new TaskException("订单金额不能为空");
        }
        if (rewardIntegral == null) {
            throw new TaskException("奖励积分不能为空");
        }

        // 核心计算逻辑：每消费numberParms元，奖励rewardIntegral积分
        BigDecimal divisor = new BigDecimal(numberParms);

        // 计算积分 = (订单金额 / 消费基数) × 奖励积分
        return orderAmount
                .divide(divisor, 10, RoundingMode.HALF_UP)
                .multiply(rewardIntegral)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
