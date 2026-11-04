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
package vip.xiaonuo.shop.modular.mallgoods.controller;

import api.shop.dto.MallGoodsSearchDTO;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.shop.modular.mallgoods.entity.MallGoods;
import vip.xiaonuo.shop.modular.mallgoods.param.*;
import vip.xiaonuo.shop.modular.mallgoods.service.MallGoodsService;

import java.io.IOException;
import java.util.List;

/**
 * 商城-商品控制器
 *
 * @author yuezisheng
 * @date  2025/02/20 15:25
 */
@Api(tags = "商城-商品控制器")
@Controller
@Valid
public class MallGoodsController {

    @Inject
    private MallGoodsService mallGoodsService;

    /**
     * 获取商城-商品分页
     *
     * @author yuezisheng
     * @date  2025/02/20 15:25
     */
    @ApiOperation("获取商城-商品分页")
    @SaCheckPermission("/shop/mallgoods/page")
    @Get
    @Mapping("/shop/mallgoods/page")
    public CommonResult<Page<MallGoods>> page(MallGoodsPageParam mallGoodsPageParam) {
        return CommonResult.data(mallGoodsService.page(mallGoodsPageParam));
    }

    /**
     * 添加商城-商品
     *
     * @author yuezisheng
     * @date  2025/02/20 15:25
     */
    @ApiOperation("添加商城-商品")
    @CommonLog("添加商城-商品")
    @SaCheckPermission("/shop/mallgoods/add")
    @Post
    @Mapping("/shop/mallgoods/add")
    public CommonResult<String> add(MallGoodsAddParam mallGoodsAddParam) {
        mallGoodsService.add(mallGoodsAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑商城-商品
     *
     * @author yuezisheng
     * @date  2025/02/20 15:25
     */
    @ApiOperation("编辑商城-商品")
    @CommonLog("编辑商城-商品")
    @SaCheckPermission("/shop/mallgoods/edit")
    @Post
    @Mapping("/shop/mallgoods/edit")
    public CommonResult<String> edit(MallGoodsEditParam mallGoodsEditParam) {
        mallGoodsService.edit(mallGoodsEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除商城-商品
     *
     * @author yuezisheng
     * @date  2025/02/20 15:25
     */
    @ApiOperation("删除商城-商品")
    @CommonLog("删除商城-商品")
    @SaCheckPermission("/shop/mallgoods/delete")
    @Post
    @Mapping("/shop/mallgoods/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<MallGoodsIdParam> mallGoodsIdParamList) {
        mallGoodsService.delete(mallGoodsIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取商城-商品详情
     *
     * @author yuezisheng
     * @date  2025/02/20 15:25
     */
    @ApiOperation("获取商城-商品详情")
    @SaCheckPermission("/shop/mallgoods/detail")
    @Get
    @Mapping("/shop/mallgoods/detail")
    public CommonResult<MallGoods> detail(MallGoodsIdParam mallGoodsIdParam) {
        return CommonResult.data(mallGoodsService.detail(mallGoodsIdParam));
    }

    /**
     * 商品下拉框选项
     */
    @ApiOperation("获取商城-商品下拉框")
    @Get
    @Mapping("/shop/mallgoods/detailList")
    public CommonResult<List<MallGoods>> detailList() {
        return CommonResult.data(mallGoodsService.detailList());
    }

    /**
     * 商品导出
     *
     * @author
     * @date
     */

    @ApiOperation("商品导出")
    @Get
    @Mapping(value = "/shop/mallgoods/export")
    public void exportUser(MallGoodsExportParam mallGoodsExportParam, Context context ) throws IOException {
        mallGoodsService.exportMallGoods(mallGoodsExportParam, context);
    }

    /**
     * 商品信息list
     */
    @ApiOperation("获取商城-商品信息")
    @SaCheckPermission("/shop/mallgoods/list")
    @Get
    @Mapping("/shop/mallgoods/list")
    public CommonResult<List<MallGoods>> MallGoodsList(MallGoodsIdAndTitleParam mallGoodsIdAndTitleParam) {
        return CommonResult.data(mallGoodsService.mallGoodsList(mallGoodsIdAndTitleParam));
    }

    @ApiOperation("商品信息搜索")
    @SaCheckPermission("/shop/mallgoods/search")
    @Get
    @Mapping("/shop/mallgoods/search")
    public CommonResult<Page<MallGoods>> search(MallGoodsSearchDTO param) {
        return CommonResult.data(mallGoodsService.search(param));
    }
}
