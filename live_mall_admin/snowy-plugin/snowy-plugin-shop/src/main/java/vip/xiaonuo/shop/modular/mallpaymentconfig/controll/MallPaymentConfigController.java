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
package vip.xiaonuo.shop.modular.mallpaymentconfig.controll;

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
import vip.xiaonuo.shop.modular.mallorder.entity.MallOrder;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderAddParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderEditParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderIdParam;
import vip.xiaonuo.shop.modular.mallorder.param.MallOrderPageParam;
import vip.xiaonuo.shop.modular.mallorder.service.MallOrderService;
import vip.xiaonuo.shop.modular.mallpaymentconfig.entity.MallPaymentConfig;
import vip.xiaonuo.shop.modular.mallpaymentconfig.param.MallPaymentConfigAddParam;
import vip.xiaonuo.shop.modular.mallpaymentconfig.param.MallPaymentConfigEditParam;
import vip.xiaonuo.shop.modular.mallpaymentconfig.param.MallPaymentConfigIdParam;
import vip.xiaonuo.shop.modular.mallpaymentconfig.param.MallPaymentConfigPageParam;
import vip.xiaonuo.shop.modular.mallpaymentconfig.service.MallPaymentConfigService;

import java.util.List;

/**
 * 商城-支付配置控制器
 *
 */
@Api(tags = "商城-支付配置控制器")
@Controller
@Valid
public class MallPaymentConfigController {

    @Inject
    private MallPaymentConfigService mallPaymentConfigService;
    /**
     * 获取商城-订单分页
     *
     */
    @ApiOperation("获取商城-订单分页")
    @SaCheckPermission("/shop/paymentconfig/page")
    @Get
    @Mapping("/shop/paymentconfig/page")
    public CommonResult<Page<MallPaymentConfig>> page(MallPaymentConfigPageParam mallPaymentConfigPageParam) {
        return CommonResult.data(mallPaymentConfigService.page(mallPaymentConfigPageParam));
    }

    /**
     * 添加商城-商品
     *
     */
    @ApiOperation("添加商城-商品")
    @CommonLog("添加商城-商品")
    @SaCheckPermission("/shop/paymentconfig/add")
    @Post
    @Mapping("/shop/paymentconfig/add")
    public CommonResult<String> add(MallPaymentConfigAddParam mallPaymentConfigAddParam) {
        mallPaymentConfigService.add(mallPaymentConfigAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑商城-商品
     *
     */
    @ApiOperation("编辑商城-商品")
    @CommonLog("编辑商城-商品")
    @SaCheckPermission("/shop/paymentconfig/edit")
    @Post
    @Mapping("/shop/paymentconfig/edit")
    public CommonResult<String> edit(MallPaymentConfigEditParam mallPaymentConfigEditParam) {
        mallPaymentConfigService.edit(mallPaymentConfigEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除商城-商品
     *
     */
    @ApiOperation("删除商城-商品")
    @CommonLog("删除商城-商品")
    @SaCheckPermission("/shop/paymentconfig/delete")
    @Post
    @Mapping("/shop/paymentconfig/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<MallPaymentConfigIdParam> mallPaymentConfigIdParamList) {
        mallPaymentConfigService.delete(mallPaymentConfigIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取商城-商品详情
     *
     */
    @ApiOperation("获取商城-商品详情")
    @SaCheckPermission("/shop/paymentconfig/detail")
    @Get
    @Mapping("/shop/paymentconfig/detail")
    public CommonResult<MallPaymentConfig> detail(MallPaymentConfigIdParam mallPaymentConfigIdParam) {
        return CommonResult.data(mallPaymentConfigService.detail(mallPaymentConfigIdParam));
    }
}
