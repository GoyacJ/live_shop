package vip.xiaonuo.shop.modular.logistics.kdniao.processor;

import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.shop.modular.logistics.kdniao.request.InstantQueryRequest;
import vip.xiaonuo.shop.modular.logistics.kdniao.request.TrajectorySubscriptionRequest;
import api.logistics.dto.InstantQueryResponse;
import vip.xiaonuo.shop.modular.logistics.kdniao.response.TrajectorySubscriptionResponse;
import api.logistics.processor.LogisticsProcessor;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/25 10:10
 */
public interface KdniaoLogisticsProcessor extends LogisticsProcessor {

    /**
     * 即时查询
     *
     * @param request 请求
     * @return 响应结果
     */
    InstantQueryResponse instantQuery(@Validated InstantQueryRequest request);

    /**
     * 轨迹订阅
     * @param request 请求
     * @return 响应
     */
    TrajectorySubscriptionResponse trajectorySubscription(@Validated TrajectorySubscriptionRequest request);

    /**
     * 轨迹推送
     *
     * @param requestData 业务值
     * @param eBusinessId 用户Id
     * @param requestType 请求接口类型
     * @param dataSign 数据签名
     * @param dataType 默认2
     * @return 响应
     */
    String trajectoryCallback(String requestData, String eBusinessId, String requestType, String dataSign, String dataType);
}
