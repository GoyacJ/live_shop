package vip.xiaonuo.mobile.modular.live.controller;

import api.order.MallOrderApi;
import api.order.dto.WechatPayCustomResultDTO;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import org.noear.solon.data.annotation.Tran;
import org.noear.solon.validation.annotation.NotNull;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.live.api.live.dto.LiveVisitDurationDTO;
import vip.xiaonuo.mobile.api.live.LiveConfigurationApi;
import vip.xiaonuo.mobile.api.order.PlaceOrderApi;
import vip.xiaonuo.mobile.api.order.result.OrderListResult;
import vip.xiaonuo.mobile.modular.live.param.OrderCreateParam;
import vip.xiaonuo.mobile.modular.live.param.UserVisitLiveTimeParam;
import vip.xiaonuo.mobile.modular.live.result.HomeDataResult;
import vip.xiaonuo.mobile.modular.live.service.LiveService;

import java.util.List;
import java.util.Map;

@Api(tags = "直播控制器")
@Controller
@Valid
@SaIgnore
public class LiveController {

    @Inject
    private LiveConfigurationApi liveConfigurationApi;
    @Inject
    private LiveService liveService;
    @Inject
    private PlaceOrderApi placeOrderApi;

    @Inject
    private MallOrderApi mallOrderApi;

    @ApiOperation("观看视频")
    @Post
    @Mapping("/view-duration")
    public CommonResult<Void> viewDuration(LiveVisitDurationDTO dto) {
        liveService.viewDuration(dto);
        return CommonResult.ok();
    }

    @ApiOperation("获取直播信息")
    @Get
    @Tran(readOnly = true)
    // @Mapping("/live/configuration/getLiveInformation")
    @Mapping("/mapi/live/detail")
    public CommonResult<Map<String, Object>> getLiveInformation(@NotNull String liveId) {
        return CommonResult.data(liveConfigurationApi.getLiveInformation(liveId));
    }

    @Get
    @Mapping("/mapi/live/test")
    public CommonResult<Map<String, String>> liveTest() {
        return CommonResult.data(liveConfigurationApi.getLiveUrl());
    }

    @ApiOperation("创建福袋")
    @Post
    @Tran
    @Mapping("/live/configuration/createFortune")
    public CommonResult<Map<String, String>> createFortune(@NotNull String peopleCount, @NotNull String price, @NotNull String seconds) {
        return CommonResult.data(liveConfigurationApi.createFortune(peopleCount, price, seconds));
    }

    @ApiOperation("获取福袋")
    @Post
    @Tran
    @Mapping("/live/configuration/getFortune")
    public CommonResult<Map<String, String>> getFortune(@NotNull String FortuneId) {
        return CommonResult.data(liveConfigurationApi.getFortune(FortuneId));
    }

    @ApiOperation("获取app分享海报")
    @Post
    @Mapping("/mapi/auth/getAppSharingLinks")
    public CommonResult<String> getAppSharingLinks() {
        return CommonResult.data(liveConfigurationApi.getAppSharingLinks());
    }

    @ApiOperation("获取live分享海报")
    @Post
    @Mapping("/mapi/auth/getLiveSharingLinks")
    public CommonResult<Map<String, String>> getLiveSharingLinks() {
        return CommonResult.data(liveConfigurationApi.getLiveSharingLinks());
    }


    @ApiOperation("获取app首页数据")
    @Get
    @Mapping("/mapi/getHomeData")
    public CommonResult<HomeDataResult> getHomeData() {
        return CommonResult.data(liveService.getHomeData());
    }

    @Get
    @Mapping("/mapi/auth/order/baseInfo")
    public CommonResult<JSONObject> orderBaseInfo() {
        return CommonResult.data(liveService.getOrderBaseInfo());
    }

    @ApiOperation("下订单")
    @Post
    @Tran
    @Mapping("/mapi/auth/order/create")
    public CommonResult<String> orderCreate(@Validated OrderCreateParam orderCreateParam, Context context) {
        Object uid = context.attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            return CommonResult.error("用户未登录");
        }
        return placeOrderApi.playOrder(Convert.toStr(uid),
                orderCreateParam.getGoodsId(),
                orderCreateParam.getGoodsCount(),
                orderCreateParam.getAddressId(),
                orderCreateParam.getOrderType(),
                orderCreateParam.getSourceId(),
                orderCreateParam.getRemark());
    }

    @ApiOperation("订单支付")
    @Post
    @Tran
    @Mapping("/mapi/auth/order/payUrl")
    public CommonResult<Map<String, String>> orderPay(@Validated @NotNull String orderId, @Validated @NotNull String payType, Context context) {
        Object uid = context.attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            return CommonResult.error("用户未登录");
        }
        return placeOrderApi.orderPay(Convert.toStr(uid), orderId, payType);
    }

    @ApiOperation("订单支付结果查询")
    @Get
    @Mapping("/mapi/auth/order/pay-result")
    public CommonResult<WechatPayCustomResultDTO> orderPayResult(@Validated @NotNull String orderNum, Context context) {
        Object uid = context.attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            return CommonResult.error("用户未登录");
        }
        return CommonResult.data(mallOrderApi.orderPayResult(orderNum));
    }

    @ApiOperation("订单支付")
    @Post
    @Tran
    @Mapping("/mapi/auth/order/h5PayUrl")
    public CommonResult<String> h5PayUrl(@Validated @NotNull String orderId, @Validated @NotNull String payType, Context context) {
        Object uid = context.attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            return CommonResult.error("用户未登录");
        }
        return placeOrderApi.h5PayUrl(Convert.toStr(uid), orderId, payType);
    }

    @Post
    @Mapping("/mapi/auth/order/cancel")
    public CommonResult<Map<String, String>> cancelOrder(String orderId, Context context) {
        Object uid = context.attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            return CommonResult.error("用户未登录");
        }
        String userId = Convert.toStr(uid);
        return placeOrderApi.cancelOrder(userId, orderId) ? CommonResult.ok() : CommonResult.error("取消失败");
    }


    @ApiOperation("确认收货")
    @Post
    @Tran
    @Mapping("/mapi/auth/order/complete")
    public CommonResult<Map<String, String>> confirmReceipt(String orderId, Context context) {
        Object uid = context.attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            return CommonResult.error("用户未登录");
        }
        String userId = Convert.toStr(uid);
        return placeOrderApi.confirmReceipt(userId, orderId) ? CommonResult.ok() : CommonResult.error("操作失败");
    }

    @ApiOperation("评价订单")
    @Post
    @Tran
    @Mapping("/live/evaluate/evaluateOrder")
    public CommonResult<Map<String, String>> evaluateOrder(@NotNull String userId, @NotNull String productId,
                                                           @NotNull String evaluationContent) {
        return CommonResult.data(placeOrderApi.evaluateOrder(userId, productId, evaluationContent));
    }

    @ApiOperation("获取订单列表")
    @Get
    @Mapping("/mapi/auth/order/list")
    public CommonResult<List<OrderListResult>> getOrderList(String status, Context context) {
        if (ObjectUtil.isEmpty(status)) {
            return CommonResult.error("订单状态不能为空");
        }
        Object uid = context.attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            return CommonResult.error("用户未登录");
        }
        return CommonResult.data(placeOrderApi.getOrderList(Convert.toStr(uid), status));
    }

    @Get
    @Mapping("/mapi/auth/order/detail")
    public CommonResult<JSONObject> orderDetail(String orderId) {
        if (ObjectUtil.isEmpty(orderId)) {
            return CommonResult.error("订单不存在");
        }
        return CommonResult.data(placeOrderApi.getOrderDetail(orderId));
    }

    @ApiOperation("售后处理")
    @Post
    @Tran
    @Mapping("/live/after/afterSale")
    public CommonResult<Map<String, String>> afterSale(@NotNull String userId, @NotNull String orderId,
                                                       @NotNull String afterSaleType, @NotNull String afterSaleDescription) {
        return CommonResult.data(placeOrderApi.afterSale(userId, orderId, afterSaleType, afterSaleDescription));
    }

    @ApiOperation("记录分享行为并发放奖励")
    @Post
    @Tran
    @Mapping("/live/record/recordShare")
    public CommonResult<Map<String, String>> recordShare(@NotNull String shareCode, @NotNull String newUserId) {
        return CommonResult.data(placeOrderApi.recordShare(shareCode, newUserId));
    }

    @ApiOperation("观看时长")
    @Post
    @Mapping("/mapi/auth/live/user-visit-time")
    public CommonResult<Void> userVisitLiveTime(UserVisitLiveTimeParam param) {
        if (liveService.userVisitLiveTime(param)) {
            return CommonResult.ok();
        }
        return CommonResult.error();
    }
}
