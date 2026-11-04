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
package vip.xiaonuo.shop.modular.mallintegralgoods.controller;

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
import vip.xiaonuo.shop.modular.mallintegralgoods.entity.MallIntegralGoods;
import vip.xiaonuo.shop.modular.mallintegralgoods.param.MallIntegralGoodsAddParam;
import vip.xiaonuo.shop.modular.mallintegralgoods.param.MallIntegralGoodsEditParam;
import vip.xiaonuo.shop.modular.mallintegralgoods.param.MallIntegralGoodsIdParam;
import vip.xiaonuo.shop.modular.mallintegralgoods.param.MallIntegralGoodsPageParam;
import vip.xiaonuo.shop.modular.mallintegralgoods.service.MallIntegralGoodsService;

/**
 * 积分商城-商品控制器
 *
 * @author 陈金坤
 * @date  2025/04/08 14:04
 */
@Api(tags = "积分商城-商品控制器")
@Controller
@Valid
public class MallIntegralGoodsController {

    @Inject
    private MallIntegralGoodsService mallIntegralGoodsService;

    /**
     * 获取积分商城-商品分页
     *
     * @author 陈金坤
     * @date  2025/04/08 14:04
     */
    @ApiOperation("获取积分商城-商品分页")
    @SaCheckPermission("/shop/mallintegralgoods/page")
    @Get
    @Mapping("/shop/mallintegralgoods/page")
    public CommonResult<Page<MallIntegralGoods>> page(MallIntegralGoodsPageParam mallIntegralGoodsPageParam) {
        return CommonResult.data(mallIntegralGoodsService.page(mallIntegralGoodsPageParam));
    }

    /**
     * 添加积分商城-商品
     *
     * @author 陈金坤
     * @date  2025/04/08 14:04
     */
    @ApiOperation("添加积分商城-商品")
    @CommonLog("添加积分商城-商品")
    @SaCheckPermission("/shop/mallintegralgoods/add")
    @Post
    @Mapping("/shop/mallintegralgoods/add")
    public CommonResult<String> add(MallIntegralGoodsAddParam mallIntegralGoodsAddParam) {
        mallIntegralGoodsService.add(mallIntegralGoodsAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑积分商城-商品
     *
     * @author 陈金坤
     * @date  2025/04/08 14:04
     */
    @ApiOperation("编辑积分商城-商品")
    @CommonLog("编辑积分商城-商品")
    @SaCheckPermission("/shop/mallintegralgoods/edit")
    @Post
    @Mapping("/shop/mallintegralgoods/edit")
    public CommonResult<String> edit(MallIntegralGoodsEditParam mallIntegralGoodsEditParam) {
        mallIntegralGoodsService.edit(mallIntegralGoodsEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除积分商城-商品
     *
     * @author 陈金坤
     * @date  2025/04/08 14:04
     */
    @ApiOperation("删除积分商城-商品")
    @CommonLog("删除积分商城-商品")
    @SaCheckPermission("/shop/mallintegralgoods/delete")
    @Post
    @Mapping("/shop/mallintegralgoods/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<MallIntegralGoodsIdParam> mallIntegralGoodsIdParamList) {
        mallIntegralGoodsService.delete(mallIntegralGoodsIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取积分商城-商品详情
     *
     * @author 陈金坤
     * @date  2025/04/08 14:04
     */
    @ApiOperation("获取积分商城-商品详情")
    @SaCheckPermission("/shop/mallintegralgoods/detail")
    @Get
    @Mapping("/shop/mallintegralgoods/detail")
    public CommonResult<MallIntegralGoods> detail(MallIntegralGoodsIdParam mallIntegralGoodsIdParam) {
        return CommonResult.data(mallIntegralGoodsService.detail(mallIntegralGoodsIdParam));
    }
}
