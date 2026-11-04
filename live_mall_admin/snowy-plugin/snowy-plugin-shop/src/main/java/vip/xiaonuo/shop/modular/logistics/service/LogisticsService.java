package vip.xiaonuo.shop.modular.logistics.service;

import api.logistics.common.enums.Shipper;
import api.logistics.dto.InstantQueryDTO;

/**
 * 物流服务
 *
 * @Date: 2025/4/17 17:10
 * @Author: 陈金坤
 * @Description: 功能已实现快递鸟
 * @Version: 1.0.0
 **/
public interface LogisticsService {


    /**
     * 即时查询
     *
     * @param shipper      快递公司
     * @param logisticCode 快递单号
     * @param customerName 收件人手机号
     * @param orderCode 订单编号
     * @return 结果
     */
    InstantQueryDTO instantQuery(Shipper shipper, String logisticCode, String customerName, String orderCode);

    /**
     * 轨迹订阅
     *
     * @param shipper      快递公司
     * @param logisticCode 快递单号
     * @param customerName 收件人手机号后四位
     * @param orderCode 订单编号
     * @return 结果
     */
    boolean trajectorySubscription(Shipper shipper, String logisticCode, String customerName, String orderCode);

}
