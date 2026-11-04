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
package vip.xiaonuo.shop.modular.mallthirdpaymen.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.annotation.CommonWrapper;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderAddParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderEditParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderIdParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderPageParam;
import vip.xiaonuo.shop.modular.mallorder.service.MallOrderService;
import vip.xiaonuo.shop.modular.mallthirdpaymen.entity.MallThirdPayment;
import vip.xiaonuo.shop.modular.mallthirdpaymen.param.MallThirdPaymentAddParam;
import vip.xiaonuo.shop.modular.mallthirdpaymen.param.MallThirdPaymentEditParam;
import vip.xiaonuo.shop.modular.mallthirdpaymen.param.MallThirdPaymentIdParam;
import vip.xiaonuo.shop.modular.mallthirdpaymen.param.MallThirdPaymentPageParam;
import vip.xiaonuo.shop.modular.mallthirdpaymen.service.MallThirdPaymentService;
import vip.xiaonuo.shop.modular.mallthirdpaymen.wrapper.MallThirdPaymentWrapper;

/**
 * 商城-支付控制器
 *
 */
@Api(tags = "商城-支付控制器")
@Controller
@Valid
public class MallThirdPaymentController {

    @Inject
    private MallThirdPaymentService mallThirdPaymentService;
    /**
     * 获取商城-订单分页
     *
     */
    @ApiOperation("获取商城-订单分页")
    @SaCheckPermission("/shop/thirdPayment/page")
    @Get
    @Mapping("/shop/thirdPayment/page")
    @CommonWrapper(value = MallThirdPaymentWrapper.class)
    public CommonResult<Page<MallThirdPayment>> page(MallThirdPaymentPageParam mallThirdPaymentPageParam) {
        return CommonResult.data(mallThirdPaymentService.page(mallThirdPaymentPageParam));
    }

    /**
     * 添加商城-商品
     *
     */
    @ApiOperation("添加商城-商品")
    @CommonLog("添加商城-商品")
    @SaCheckPermission("/shop/thirdPayment/add")
    @Post
    @Mapping("/shop/thirdPayment/add")
    public CommonResult<String> add(MallThirdPaymentAddParam mallThirdPaymentAddParam) {
        mallThirdPaymentService.add(mallThirdPaymentAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑商城-商品
     *
     */
    @ApiOperation("编辑商城-商品")
    @CommonLog("编辑商城-商品")
    @SaCheckPermission("/shop/thirdPayment/edit")
    @Post
    @Mapping("/shop/thirdPayment/edit")
    public CommonResult<String> edit(MallThirdPaymentEditParam mallThirdPaymentEditParam) {
        mallThirdPaymentService.edit(mallThirdPaymentEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除商城-商品
     *
     */
    @ApiOperation("删除商城-商品")
    @CommonLog("删除商城-商品")
    @SaCheckPermission("/shop/thirdPayment/delete")
    @Post
    @Mapping("/shop/thirdPayment/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<MallThirdPaymentIdParam> MallThirdPaymentIdParamList) {
        mallThirdPaymentService.delete(MallThirdPaymentIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取商城-商品详情
     *
     */
    @ApiOperation("获取商城-商品详情")
    @SaCheckPermission("/shop/thirdPayment/detail")
    @Get
    @Mapping("/shop/thirdPayment/detail")
    public CommonResult<MallThirdPayment> detail(MallThirdPaymentIdParam mallThirdPaymentIdParam) {
        return CommonResult.data(mallThirdPaymentService.detail(mallThirdPaymentIdParam));
    }


}
