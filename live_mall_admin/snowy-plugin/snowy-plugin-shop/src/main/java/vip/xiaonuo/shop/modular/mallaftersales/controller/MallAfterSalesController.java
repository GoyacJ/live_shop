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
package vip.xiaonuo.shop.modular.mallaftersales.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.shop.modular.mallaftersales.entity.MallAfterSales;
import vip.xiaonuo.shop.modular.mallaftersales.param.MallAfterSalesAddParam;
import vip.xiaonuo.shop.modular.mallaftersales.param.MallAfterSalesEditParam;
import vip.xiaonuo.shop.modular.mallaftersales.param.MallAfterSalesIdParam;
import vip.xiaonuo.shop.modular.mallaftersales.param.MallAfterSalesPageParam;
import vip.xiaonuo.shop.modular.mallaftersales.result.MallAfterSalesPageResult;
import vip.xiaonuo.shop.modular.mallaftersales.service.MallAfterSalesService;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderAddParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderEditParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderIdParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderPageParam;
import vip.xiaonuo.shop.modular.mallorder.service.MallOrderService;

/**
 * 商城-售后控制器
 *
 */
@Api(tags = "商城-售后控制器")
@Controller
@Valid
public class MallAfterSalesController {

    @Inject
    private MallAfterSalesService mallAfterSalesService;
    /**
     * 获取商城-订单分页
     *
     */
    @ApiOperation("获取商城-订单分页")
    @SaCheckPermission("/shop/afterSales/page")
    @Get
    @Mapping("/shop/afterSales/page")
    public CommonResult<Page<MallAfterSalesPageResult>> page(MallAfterSalesPageParam mallAfterSalesPageParam) {
        return CommonResult.data(mallAfterSalesService.page(mallAfterSalesPageParam));
    }

    /**
     * 添加商城-商品
     *
     */
    @ApiOperation("添加商城-商品")
    @CommonLog("添加商城-商品")
    @SaCheckPermission("/shop/afterSales/add")
    @Post
    @Mapping("/shop/afterSales/add")
    public CommonResult<String> add(MallAfterSalesAddParam mallAfterSalesAddParam) {
        mallAfterSalesService.add(mallAfterSalesAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑商城-商品
     *
     */
    @ApiOperation("编辑商城-商品")
    @CommonLog("编辑商城-商品")
    @SaCheckPermission("/shop/afterSales/edit")
    @Post
    @Mapping("/shop/afterSales/edit")
    public CommonResult<String> edit(MallAfterSalesEditParam mallAfterSalesEditParam) {
        mallAfterSalesService.edit(mallAfterSalesEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除商城-商品
     *
     */
    @ApiOperation("删除商城-商品")
    @CommonLog("删除商城-商品")
    @SaCheckPermission("/shop/afterSales/delete")
    @Post
    @Mapping("/shop/afterSales/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<MallAfterSalesIdParam> mallAfterSalesIdParamList) {
        mallAfterSalesService.delete(mallAfterSalesIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取商城-商品详情
     *
     */
    @ApiOperation("获取商城-商品详情")
    @SaCheckPermission("/shop/afterSales/detail")
    @Get
    @Mapping("/shop/afterSales/detail")
    public CommonResult<MallAfterSales> detail(MallAfterSalesIdParam mallAfterSalesIdParam) {
        return CommonResult.data(mallAfterSalesService.detail(mallAfterSalesIdParam));
    }

}
