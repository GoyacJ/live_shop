package vip.xiaonuo.shop.modular.logistics.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.shop.modular.logistics.kdniao.processor.KdniaoLogisticsProcessor;
import vip.xiaonuo.shop.modular.logistics.kdniao.request.InstantQueryRequest;
import vip.xiaonuo.shop.modular.logistics.kdniao.request.TrajectorySubscriptionRequest;
import api.logistics.dto.InstantQueryResponse;
import vip.xiaonuo.shop.modular.logistics.kdniao.response.TrajectorySubscriptionResponse;

/**
 * <p>快递鸟物流</p>
 * <p>
 * ----------
 * * 轨迹推送接口: 推送规则
 * 1.返回的物流轨迹信息按照发生时间的升序排列。
 * 2.轨迹推送是异步推送的，系统拉取到轨迹后调用用户推送接口推送轨迹数据。
 * 3.若Data接收到推送Success为false，且Reason为三天无轨迹，需重新发起订阅。
 * 4.若Data接收到推送Success为false，且Reason为七天内无轨迹变化，需重新发起订阅。
 * 5.若Data接收到推送Success为false，且State为0，属于正常推送，需耐心等待下次推送即可。
 * 6.在没有返回信息或者返回不符合规定的情况下，会判断为推送失败。推送失败后会重试推送2次，首次间隔15分钟，二次间隔半个小时，
 * 两次都失败就不会再推送，直到下次有轨迹更新才会再次推送，建议您严格按照技术文档进行返回。
 * <p>
 * 每次全量推送，一次最多推送10单
 * ----------
 *
 * @author goya
 * @since 2025/4/25 11:21
 */
@Api(tags = "快递鸟控制器")
@Controller
@Valid
@Mapping("/kdniao")
public class KdniaoLogisticsController {

    @Inject
    private KdniaoLogisticsProcessor kdniaoLogisticsProcessor;

    /**
     * 轨迹推送
     */
    @ApiOperation("轨迹推送回调")
    @Post
    @Mapping("/trajectory/callback")
    public String trajectoryCallback(@Param("RequestData") String requestData,
                                     @Param("EBusinessID") String eBusinessId,
                                     @Param("RequestType") String requestType,
                                     @Param("DataSign") String dataSign,
                                     @Param("DataType") String dataType) {
        return kdniaoLogisticsProcessor.trajectoryCallback(requestData, eBusinessId, requestType, dataSign, dataType);
    }

    /**
     * 即时查询
     */
    @ApiOperation("即时查询")
    @Post
    @Mapping("/instant/query")
    public CommonResult<InstantQueryResponse> instantQuery(@Validated @Body InstantQueryRequest request) {
        InstantQueryResponse response = kdniaoLogisticsProcessor.instantQuery(request);
        return CommonResult.data(response);
    }

    /**
     * 轨迹订阅
     */
    @ApiOperation("轨迹订阅")
    @Post
    @Mapping("/trajectory/subscription")
    public CommonResult<TrajectorySubscriptionResponse> trajectoryPush(@Validated @Body TrajectorySubscriptionRequest request) {
        TrajectorySubscriptionResponse response = kdniaoLogisticsProcessor.trajectorySubscription(request);
        return CommonResult.data(response);
    }
}
