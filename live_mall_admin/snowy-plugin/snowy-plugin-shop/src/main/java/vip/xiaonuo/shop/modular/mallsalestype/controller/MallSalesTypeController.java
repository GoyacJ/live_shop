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
package vip.xiaonuo.shop.modular.mallsalestype.controller;

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
import vip.xiaonuo.shop.modular.mallsalestype.entity.MallSalesType;
import vip.xiaonuo.shop.modular.mallsalestype.param.MallSalesTypeAddParam;
import vip.xiaonuo.shop.modular.mallsalestype.param.MallSalesTypeEditParam;
import vip.xiaonuo.shop.modular.mallsalestype.param.MallSalesTypeIdParam;
import vip.xiaonuo.shop.modular.mallsalestype.param.MallSalesTypeSelectorParam;
import vip.xiaonuo.shop.modular.mallsalestype.service.MallSalesTypeService;

import java.util.List;

/**
 * 商城-订单控制器
 *
 */
@Api(tags = "商城-售后类型控制器")
@Controller
@Valid
public class MallSalesTypeController {

    @Inject
    private MallSalesTypeService mallSalesTypeService;

    /**
     * 添加商城-商品
     *
     */
    @ApiOperation("添加商城-售后类型")
    @CommonLog("添加商城-售后类型")
    @SaCheckPermission("/shop/salestype/add")
    @Post
    @Mapping("/shop/salestype/add")
    public CommonResult<String> add(MallSalesTypeAddParam mallSalesTypeAddParam) {
        mallSalesTypeService.add(mallSalesTypeAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑商城-商品
     *
     */
    @ApiOperation("编辑商城-售后类型")
    @CommonLog("编辑商城-售后类型")
    @SaCheckPermission("/shop/salestype/edit")
    @Post
    @Mapping("/shop/salestype/edit")
    public CommonResult<String> edit(MallSalesTypeEditParam mallSalesTypeEditParam) {
        mallSalesTypeService.edit(mallSalesTypeEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除商城-商品
     *
     */
    @ApiOperation("删除商城-售后类型")
    @CommonLog("删除商城-售后类型")
    @SaCheckPermission("/shop/salestype/delete")
    @Post
    @Mapping("/shop/salestype/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<MallSalesTypeIdParam> MallSalesTypeIdParamList) {
        mallSalesTypeService.delete(MallSalesTypeIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取商城-商品详情
     *
     */
    @ApiOperation("获取商城-售后类型详情")
    @SaCheckPermission("/shop/salestype/detail")
    @Get
    @Mapping("/shop/salestype/detail")
    public CommonResult<MallSalesType> detail(MallSalesTypeIdParam mallSalesTypeIdParam) {
        return CommonResult.data(mallSalesTypeService.detail(mallSalesTypeIdParam));
    }

    /**
     * 商品信息list
     */
    @ApiOperation("获取商城-售后类型信息")
    @SaCheckPermission("/shop/salestype/list")
    @Get
    @Mapping("/shop/salestype/list")
    public CommonResult<List<MallSalesType>> MallOrderList(MallSalesTypeIdParam mallSalesTypeIdParam) {
        return CommonResult.data(mallSalesTypeService.getMallSalesTypeList(mallSalesTypeIdParam));
    }

    /**
     * 获取选择器
     *
     */
    @ApiOperation("获取售后类型选择器")
    @Get@Mapping("/sys/salestype/mallSalesTypeSelector")
    public CommonResult<Page<MallSalesType>> salesTypeSelector(MallSalesTypeSelectorParam mallSalesTypeSelectorParam) {
        return CommonResult.data(mallSalesTypeService.salesTypeSelector(mallSalesTypeSelectorParam));
    }
}
