/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.shop.modular.mallorder.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.*;
import org.noear.solon.boot.web.MimeType;
import org.noear.solon.core.handle.Context;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.enums.OrderTypeEnum;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.live.api.live.LiveApi;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.param.*;
import vip.xiaonuo.shop.modular.mallorder.service.MallOrderService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商城-订单控制器
 */
@Slf4j
@Api(tags = "商城-订单控制器")
@Controller
@Valid
public class MallOrderController {

    @Inject
    private MallOrderService mallOrderService;
    @Inject
    private LiveApi liveApi;

    /**
     * 获取商城-订单分页
     */
    @ApiOperation("获取商城-订单分页")
    @SaCheckPermission("/shop/order/page")
    @Get
    @Mapping("/shop/order/page")
    public CommonResult<Page<MallOrder>> page(MallOrderPageParam mallOrderPageParam) {
        mallOrderPageParam.setOrderType(OrderTypeEnum.SHOP_ORDER.getType());
        return CommonResult.data(mallOrderService.page(mallOrderPageParam));
    }

    @ApiOperation("获取积分商城-订单分页")
    @SaCheckPermission("/shop/order/integral/page")
    @Get
    @Mapping("/shop/order/integral/page")
    public CommonResult<Page<MallOrder>> integralPage(MallOrderPageParam mallOrderPageParam) {
        mallOrderPageParam.setOrderType(OrderTypeEnum.INTEGRAL_ORDER.getType());
        return CommonResult.data(mallOrderService.page(mallOrderPageParam));
    }

    @ApiOperation("获取直播间-订单分页")
    @SaCheckPermission("/shop/order/live/page")
    @Get
    @Mapping("/shop/order/live/page")
    public CommonResult<Page<MallOrder>> livePage(MallOrderPageParam mallOrderPageParam) {
        mallOrderPageParam.setOrderType(OrderTypeEnum.LIVE_ORDER.getType());
        Page<MallOrder> page = mallOrderService.page(mallOrderPageParam);
        if (page != null || ObjectUtil.isNotEmpty(page.getRecords())) {
            Set<String> liveIds = page.getRecords().stream()
                    .filter(oreder -> StringUtils.isNotBlank(oreder.getOrderSourceId()))
                    .map(MallOrder::getOrderSourceId)
                    .collect(Collectors.toSet());
            // 查询直播间标题并转换
            if (ObjectUtil.isNotEmpty(liveIds)) {
                List<JSONObject> liveList = liveApi.getLiveBizByIds(liveIds);
                Map<String, String> liveMap = CollStreamUtil.toMap(liveList, o -> o.getStr("id"), o -> o.getStr("mainTitle"));
                page.getRecords().forEach(order -> {
                    order.setOrderSourceName(liveMap.get(order.getOrderSourceId()));
                });
            }
        }
        return CommonResult.data(page);
    }

    /**
     * 添加商城-商品
     */
    @ApiOperation("创建商城订单")
    @CommonLog("创建商城订单")
    @SaCheckPermission("/shop/order/add")
    @Post
    @Mapping("/shop/order/add")
    public CommonResult<String> add(MallOrderAddParam mallOrderAddParam) {
        mallOrderService.add(mallOrderAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑商城-商品
     */
    @ApiOperation("编辑商城-商品")
    @CommonLog("编辑商城-商品")
    @SaCheckPermission("/shop/order/edit")
    @Post
    @Mapping("/shop/order/edit")
    public CommonResult<String> edit(MallOrderEditParam mallOrderEditParam) {
        mallOrderService.edit(mallOrderEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除商城-商品
     */
    @ApiOperation("删除商城-商品")
    @CommonLog("删除商城-商品")
    @SaCheckPermission("/shop/order/delete")
    @Post
    @Mapping("/shop/order/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                       CommonValidList<MallOrderIdParam> MallOrderIdParamList) {
        mallOrderService.delete(MallOrderIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取商城-商品详情
     */
    @ApiOperation("获取商城-商品详情")
    @SaCheckPermission("/shop/order/detail")
    @Get
    @Mapping("/shop/order/detail")
    public CommonResult<MallOrder> detail(MallOrderIdParam mallOrderIdParam) {
        return CommonResult.data(mallOrderService.detail(mallOrderIdParam));
    }

    /**
     * 商品信息list
     */
    @ApiOperation("获取商城-订单信息")
    @SaCheckPermission("/shop/order/list")
    @Get
    @Mapping("/shop/order/list")
    public CommonResult<List<MallOrder>> mallOrderList(MallOrderIdParam mallOrderIdParam) {
        return CommonResult.data(mallOrderService.mallOrderList(mallOrderIdParam));
    }

    @ApiOperation("订单信息导出")
    @CommonLog("订单信息导出")
    @SaCheckPermission("/shop/order/export")
    @Get
    @Produces(MimeType.APPLICATION_OCTET_STREAM_VALUE)
    @Mapping("/shop/order/export")
    public void exportOrders(MallExportOrdersParam param, Context ctx) {
        mallOrderService.exportOrders(param.orderIds(),ctx);
    }
}
