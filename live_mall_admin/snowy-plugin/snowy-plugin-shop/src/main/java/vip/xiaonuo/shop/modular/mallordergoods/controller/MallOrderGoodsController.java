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
package vip.xiaonuo.shop.modular.mallordergoods.controller;

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
import vip.xiaonuo.shop.modular.mallordergoods.entity.MallOrderGoods;
import vip.xiaonuo.shop.modular.mallordergoods.param.*;
import vip.xiaonuo.shop.modular.mallordergoods.service.MallOrderGoodsService;

import java.util.List;

/**
 * 商城-订单控制器
 *
 */
@Api(tags = "商城-订单商品关联控制器")
@Controller
@Valid
public class MallOrderGoodsController {

    @Inject
    private MallOrderGoodsService mallOrderGoodsService;
    /**
     * 获取商城-订单分页
     *
     */
    @ApiOperation("获取商城-订单商品分页")
    @SaCheckPermission("/shop/ordergoods/page")
    @Get
    @Mapping("/shop/ordergoods/page")
    public CommonResult<Page<MallOrderGoods>> page(MallOrderGoodsPageParam mallOrderGoodsPageParam) {
        return CommonResult.data(mallOrderGoodsService.page(mallOrderGoodsPageParam));
    }

    /**
     * 添加商城-商品
     *
     */
    @ApiOperation("添加商城-商品")
    @CommonLog("添加商城-商品")
    @SaCheckPermission("/shop/ordergoods/add")
    @Post
    @Mapping("/shop/ordergoods/add")
    public CommonResult<String> add(MallOrderGoodsAddParam mallOrderGoodsAddParam) {
        mallOrderGoodsService.add(mallOrderGoodsAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑商城-商品
     *
     */
    @ApiOperation("编辑商城-商品")
    @CommonLog("编辑商城-商品")
    @SaCheckPermission("/shop/ordergoods/edit")
    @Post
    @Mapping("/shop/ordergoods/edit")
    public CommonResult<String> edit(MallOrderGoodsEditParam mallOrderGoodsEditParam) {
        mallOrderGoodsService.edit(mallOrderGoodsEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除商城-商品
     *
     */
    @ApiOperation("删除商城-商品")
    @CommonLog("删除商城-商品")
    @SaCheckPermission("/shop/ordergoods/delete")
    @Post
    @Mapping("/shop/ordergoods/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<MallOrderGoodsIdParam> mallOrderGoodsIdParamList) {
        mallOrderGoodsService.delete(mallOrderGoodsIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取商城-商品详情
     *
     */
    @ApiOperation("获取商城-商品详情")
    @SaCheckPermission("/shop/ordergoods/detail")
    @Get
    @Mapping("/shop/ordergoods/detail")
    public CommonResult<MallOrderGoods> detail(MallOrderGoodsIdParam mallOrderGoodsIdParam) {
        return CommonResult.data(mallOrderGoodsService.detail(mallOrderGoodsIdParam));
    }

    /**
     * 获取商城-根据订单id获取关联信息
     *
     */
    @ApiOperation("获取商城-根据订单id获取关联信息")
    @SaCheckPermission("/shop/ordergoods/ordergoodslistbyorderid")
    @Get
    @Mapping("/shop/ordergoods/ordergoodslistbyorderid")
    public CommonResult<List<MallOrderGoods>> getOrderGoodsListByOrderId(MallOrderGoodsIdsParam mallOrderGoodsIdsParam) {
        return CommonResult.data(mallOrderGoodsService.getOrderGoodsListByOrderId(mallOrderGoodsIdsParam));
    }

    /**
     * 获取商城-根据商品id获取关联信息
     *
     */
    @ApiOperation("获取商城-根据商品id获取关联信息")
    @SaCheckPermission("/shop/ordergoods/ordergoodslistbygoodsid")
    @Get
    @Mapping("/shop/ordergoods/ordergoodslistbygoodsid")
    public CommonResult<List<MallOrderGoods>> getOrderGoodsListByGoodsId(MallOrderGoodsIdsParam mallOrderGoodsIdsParam) {
        return CommonResult.data(mallOrderGoodsService.getOrderGoodsListByGoodsId(mallOrderGoodsIdsParam));
    }
}
