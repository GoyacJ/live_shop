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
package vip.xiaonuo.shop.modular.mallgoodsChange.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.shop.modular.mallgoods.param.MallGoodsAddParam;
import vip.xiaonuo.shop.modular.mallgoodsChange.entity.MallGoodsChange;
import vip.xiaonuo.shop.modular.mallgoodsChange.param.MallGoodsChangeAddParam;
import vip.xiaonuo.shop.modular.mallgoodsChange.param.MallGoodsIdParam;
import vip.xiaonuo.shop.modular.mallgoodsChange.service.MallGoodsChangeService;

import java.util.List;

/**
 * 商城-商品控制器
 *
 */
@Api(tags = "商城-商品变更记录控制")
@Controller
@Valid
public class MallGoodsChangeController {

    @Inject
    private MallGoodsChangeService mallGoodsChangeService;

    /**
     * 商品变更信息list
     */
    @ApiOperation("获取商城-商品变更信息")
    @SaCheckPermission("/shop/mallgoodsChange/list")
    @Get
    @Mapping("/shop/mallgoodsChange/list")
    public CommonResult<List<MallGoodsChange>> MallGoodschangeList(MallGoodsIdParam mallGoodsIdParam) {
        return CommonResult.data(mallGoodsChangeService.mallGoodsChangeList(mallGoodsIdParam));
    }

    /**
     * 商品变更信息最新详情
     */
    @ApiOperation("获取商城-商品变更信息最新详情")
    @SaCheckPermission("/shop/mallgoodsChange/detail")
    @Get
    @Mapping("/shop/mallgoodsChange/detail")
    public CommonResult<MallGoodsChange> MallGoodschangeOnedetail(MallGoodsIdParam mallGoodsIdParam) {
        return CommonResult.data(mallGoodsChangeService.mallGoodschangeOneDetail(mallGoodsIdParam));
    }

    /**
     * 商品变更信息最新详情
     */
    @CommonLog("获取商城-新增商品变更信息")
    @SaCheckPermission("/shop/mallgoodsChange/add")
    @Post
    @Mapping("/shop/mallgoodsChange/add")
    public CommonResult<MallGoodsChange> MallGoodschangeAdd(MallGoodsChangeAddParam mallGoodsChangeAddParam) {
        mallGoodsChangeService.mallGoodschangeAdd(mallGoodsChangeAddParam);
        return CommonResult.ok();
    }

}
