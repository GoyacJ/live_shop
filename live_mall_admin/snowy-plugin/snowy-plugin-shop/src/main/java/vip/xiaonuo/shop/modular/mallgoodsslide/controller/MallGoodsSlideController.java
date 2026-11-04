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
package vip.xiaonuo.shop.modular.mallgoodsslide.controller;

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
import vip.xiaonuo.shop.modular.mallgoodsslide.entity.MallGoodsSlide;
import vip.xiaonuo.shop.modular.mallgoodsslide.param.MallGoodsSlideAddParam;
import vip.xiaonuo.shop.modular.mallgoodsslide.param.MallGoodsSlideEditParam;
import vip.xiaonuo.shop.modular.mallgoodsslide.param.MallGoodsSlideIdParam;
import vip.xiaonuo.shop.modular.mallgoodsslide.param.MallGoodsSlidePageParam;
import vip.xiaonuo.shop.modular.mallgoodsslide.service.MallGoodsSlideService;

import java.util.List;

/**
 * 商品轮播图控制器
 *
 * @author yuezisheng
 * @date  2025/02/20 15:30
 */
@Api(tags = "商品轮播图控制器")
@Controller
@Valid
public class MallGoodsSlideController {

    @Inject
    private MallGoodsSlideService mallGoodsSlideService;

    /**
     * 获取商品轮播图分页
     *
     * @author yuezisheng
     * @date  2025/02/20 15:30
     */
    @ApiOperation("获取商品轮播图分页")
    @SaCheckPermission("/shop/mallgoodsslide/page")
    @Get
    @Mapping("/shop/mallgoodsslide/page")
    public CommonResult<Page<MallGoodsSlide>> page(MallGoodsSlidePageParam mallGoodsSlidePageParam) {
        return CommonResult.data(mallGoodsSlideService.page(mallGoodsSlidePageParam));
    }

    /**
     * 添加商品轮播图
     *
     * @author yuezisheng
     * @date  2025/02/20 15:30
     */
    @ApiOperation("添加商品轮播图")
    @CommonLog("添加商品轮播图")
    @SaCheckPermission("/shop/mallgoodsslide/add")
    @Post
    @Mapping("/shop/mallgoodsslide/add")
    public CommonResult<String> add(MallGoodsSlideAddParam mallGoodsSlideAddParam) {
        mallGoodsSlideService.add(mallGoodsSlideAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑商品轮播图
     *
     * @author yuezisheng
     * @date  2025/02/20 15:30
     */
    @ApiOperation("编辑商品轮播图")
    @CommonLog("编辑商品轮播图")
    @SaCheckPermission("/shop/mallgoodsslide/edit")
    @Post
    @Mapping("/shop/mallgoodsslide/edit")
    public CommonResult<String> edit(MallGoodsSlideEditParam mallGoodsSlideEditParam) {
        mallGoodsSlideService.edit(mallGoodsSlideEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除商品轮播图
     *
     * @author yuezisheng
     * @date  2025/02/20 15:30
     */
    @ApiOperation("删除商品轮播图")
    @CommonLog("删除商品轮播图")
    @SaCheckPermission("/shop/mallgoodsslide/delete")
    @Post
    @Mapping("/shop/mallgoodsslide/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<MallGoodsSlideIdParam> mallGoodsSlideIdParamList) {
        mallGoodsSlideService.delete(mallGoodsSlideIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取商品轮播图详情
     *
     * @author yuezisheng
     * @date  2025/02/20 15:30
     */
    @ApiOperation("获取商品轮播图详情")
    @SaCheckPermission("/shop/mallgoodsslide/detail")
    @Get
    @Mapping("/shop/mallgoodsslide/detail")
    public CommonResult<MallGoodsSlide> detail(MallGoodsSlideIdParam mallGoodsSlideIdParam) {
        return CommonResult.data(mallGoodsSlideService.detail(mallGoodsSlideIdParam));
    }

    /**
     * 根据商品id查询图片信息
     *
     */
    @ApiOperation("根据商品id查询图片信息")
    @SaCheckPermission("/shop/mallgoodsslide/queryEntityListByGoodsId")
    @Get
    @Mapping("/shop/mallgoodsslide/queryEntityListByGoodsId")
    public CommonResult<List<MallGoodsSlide>> queryEntityListByGoodsId(MallGoodsSlideIdParam mallGoodsSlideIdParam) {
        return CommonResult.data(mallGoodsSlideService.queryEntityListByGoodsId(mallGoodsSlideIdParam));
    }

    /**
     * 根据商品id删除图片信息
     *
     */
    @ApiOperation("根据商品id删除图片信息")
    @CommonLog("根据商品id删除图片信息")
    @SaCheckPermission("/shop/mallgoodsslide/deleteEntityListByGoodsId")
    @Post
    @Mapping("/shop/mallgoodsslide/deleteEntityListByGoodsId")
    public CommonResult<String> deleteEntityListByGoodsId(MallGoodsSlideIdParam mallGoodsSlideIdParam) {
        mallGoodsSlideService.deleteEntityListByGoodsId(mallGoodsSlideIdParam);
        return CommonResult.ok();
    }
}
