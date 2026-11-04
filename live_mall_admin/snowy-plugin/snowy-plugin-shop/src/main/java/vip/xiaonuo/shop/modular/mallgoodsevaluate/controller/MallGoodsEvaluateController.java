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
package vip.xiaonuo.shop.modular.mallgoodsevaluate.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.shop.modular.mallgoods.entity.MallGoods;
import vip.xiaonuo.shop.modular.mallgoods.param.MallGoodsIdParam;
import vip.xiaonuo.shop.modular.mallgoods.param.MallGoodsPageParam;
import vip.xiaonuo.shop.modular.mallgoodsChange.entity.MallGoodsChange;
import vip.xiaonuo.shop.modular.mallgoodsChange.param.MallGoodsChangeAddParam;
import vip.xiaonuo.shop.modular.mallgoodsChange.service.MallGoodsChangeService;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.entity.MallGoodsEvaluate;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.param.*;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.result.MallGoodsEvaluateResult;
import vip.xiaonuo.shop.modular.mallgoodsevaluate.service.MallGoodsEvaluateService;

import java.util.List;

/**
 * 商城-商品控制器
 *
 */
@Api(tags = "商城-商品评价控制")
@Controller
@Valid
public class MallGoodsEvaluateController {

    @Inject
    private MallGoodsEvaluateService mallGoodsEvaluateService;


    /**
     * 获取商城-商品评价信息分页
     *
     */
    @ApiOperation("获取商城-商品评价信息分页")
    @SaCheckPermission("/shop/mallgoodsEvaluate/page")
    @Get
    @Mapping("/shop/mallgoodsEvaluate/page")
    public CommonResult<Page<MallGoodsEvaluateResult>> page(MallGoodsEvaluatePageParam mallGoodsEvaluatePageParam) {
        return CommonResult.data(mallGoodsEvaluateService.page(mallGoodsEvaluatePageParam));
    }


    /**
     * 商品评价信息list
     */
    @ApiOperation("获取商城-商品评价信息")
    @SaCheckPermission("/shop/mallgoodsEvaluate/list")
    @Get
    @Mapping("/shop/mallgoodsEvaluate/list")
    public CommonResult<List<MallGoodsEvaluate>> MallGoodsEvaluateList(MallGoodsIdForEvaluateParam mallGoodsIdForEvaluateParam) {
        return CommonResult.data(mallGoodsEvaluateService.mallGoodsEvaluateList(mallGoodsIdForEvaluateParam));
    }

    /**
     * 商品评价信息最新详情
     */
    @ApiOperation("获取商城-商品评价信息最新详情")
    @SaCheckPermission("/shop/mallgoodsEvaluate/detail")
    @Get
    @Mapping("/shop/mallgoodsEvaluate/detail")
    public CommonResult<MallGoodsEvaluate> MallGoodschangeOnedetail(MallGoodsIdForEvaluateParam mallGoodsIdForEvaluateParam) {
        return CommonResult.data(mallGoodsEvaluateService.mallGoodsEvaluateOneDetail(mallGoodsIdForEvaluateParam));
    }

    /**
     * 新增商品变更信息
     */
    @CommonLog("获取商城-新增商品变更信息")
    @SaCheckPermission("/shop/mallgoodsEvaluate/add")
    @Post
    @Mapping("/shop/mallgoodsEvaluate/add")
    public CommonResult<MallGoodsEvaluate> MallGoodsEvaluateAdd(MallGoodsEvaluateAddParam mallGoodsEvaluateAddParam) {
        mallGoodsEvaluateService.mallGoodsEvaluateAdd(mallGoodsEvaluateAddParam);
        //判断评价信息是否需要脱敏处理,如果需要则存两次信息
        if (SensitiveWordHelper.contains(mallGoodsEvaluateAddParam.getEvaluateInformation())){
            String evaluateInformation = SensitiveWordHelper.replace(mallGoodsEvaluateAddParam.getEvaluateInformation());
            mallGoodsEvaluateAddParam.setEvaluateInformation(evaluateInformation);
            mallGoodsEvaluateService.mallGoodsEvaluateAdd(mallGoodsEvaluateAddParam);
        }
        return CommonResult.ok();
    }

    /**
     * 修改商品变更信息
     */
    @CommonLog("获取商城-新增商品变更信息")
    @SaCheckPermission("/shop/mallgoodsEvaluate/edit")
    @Post
    @Mapping("/shop/mallgoodsEvaluate/edit")
    public CommonResult<MallGoodsEvaluate> MallGoodsEvaluateEdit(MallGoodsEvaluateEditParam mallGoodsEvaluateEditParam) {
        mallGoodsEvaluateService.mallGoodsEvaluateEdit(mallGoodsEvaluateEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除商品评价
     *
     */
    @ApiOperation("删除商品评价")
    @CommonLog("删除商品评价")
    @SaCheckPermission("/shop/mallgoodsEvaluate/delete")
    @Post
    @Mapping("/shop/mallgoodsEvaluate/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                       CommonValidList<MallGoodsEvaluateIdParam> mallGoodsEvaluateIdParamList) {
        mallGoodsEvaluateService.delete(mallGoodsEvaluateIdParamList);
        return CommonResult.ok();
    }

    /**
     * 显示评价
     *
     **/
    @ApiOperation("显示评价")
    @CommonLog("显示评价")
    @Post
    @Mapping("/shop/mallgoodsEvaluate/enablestatus")
    public CommonResult<String> enablestatus(MallGoodsEvaluateIdParam mallGoodsEvaluateIdParam) {
        mallGoodsEvaluateService.enablestatus(mallGoodsEvaluateIdParam);
        return CommonResult.ok();
    }

    /**
     * 禁用评价
     *
     **/
    @ApiOperation("禁用评价")
    @CommonLog("禁用评价")
    @Post
    @Mapping("/shop/mallgoodsEvaluate/disablestatus")
    public CommonResult<String> disablestatus(@Validated MallGoodsEvaluateIdParam mallGoodsEvaluateIdParam) {
        mallGoodsEvaluateService.disablestatus(mallGoodsEvaluateIdParam);
        return CommonResult.ok();
    }
}
