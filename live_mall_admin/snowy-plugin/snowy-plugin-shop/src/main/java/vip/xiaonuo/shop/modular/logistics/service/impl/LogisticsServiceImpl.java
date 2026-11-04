package vip.xiaonuo.shop.modular.logistics.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import api.logistics.common.enums.Shipper;
import api.logistics.dto.InstantQueryDTO;
import vip.xiaonuo.shop.modular.logistics.kdniao.processor.KdniaoLogisticsProcessor;
import vip.xiaonuo.shop.modular.logistics.kdniao.request.InstantQueryRequest;
import vip.xiaonuo.shop.modular.logistics.kdniao.request.TrajectorySubscriptionRequest;
import api.logistics.dto.InstantQueryResponse;
import vip.xiaonuo.shop.modular.logistics.kdniao.response.TrajectorySubscriptionResponse;
import vip.xiaonuo.shop.modular.logistics.service.LogisticsService;

/**
 * @Date: 2025/4/17 17:10
 * @Author: 陈金坤
 * @Description: 功能已实现快递鸟
 * @Version: 1.0.0
 **/
@Slf4j
@Component
public class LogisticsServiceImpl implements LogisticsService {

    @Inject
    private KdniaoLogisticsProcessor kdniaoLogisticsProcessor;

    @Override
    public InstantQueryDTO instantQuery(Shipper shipper, String logisticCode, String customerName, String orderCode) {
        InstantQueryResponse response = kdniaoLogisticsProcessor.instantQuery(InstantQueryRequest.of(shipper, logisticCode, customerName, orderCode));
        return InstantQueryDTO.of(response);
    }

    @Override
    public boolean trajectorySubscription(Shipper shipper, String logisticCode, String customerName, String orderCode) {
        // 使用快递鸟
        TrajectorySubscriptionResponse response = kdniaoLogisticsProcessor
                .trajectorySubscription(TrajectorySubscriptionRequest.of(shipper, logisticCode, customerName, orderCode));
        return response.success();
    }
}