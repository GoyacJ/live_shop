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
package vip.xiaonuo.live.modular.shoppingcar.controller;

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
import vip.xiaonuo.live.modular.shoppingcar.entity.ShoppingCar;
import vip.xiaonuo.live.modular.shoppingcar.param.*;
import vip.xiaonuo.live.modular.shoppingcar.service.ShoppingCarService;

/**
 * 直播间购物车表控制器
 *
 * @author lgq
 * @date  2025/03/04 14:17
 */
@Api(tags = "直播间购物车表控制器")
@Controller
@Valid
public class ShoppingCarController {

    @Inject
    private ShoppingCarService shoppingCarService;

    /**
     * 获取直播间购物车表分页
     *
     * @author lgq
     * @date  2025/03/04 14:17
     */
    @ApiOperation("获取直播间购物车表分页")
    @SaCheckPermission("/live/shoppingcar/page")
    @Get
    @Mapping("/live/shoppingcar/page")
    public CommonResult<Page<ShoppingCar>> page(ShoppingCarPageParam shoppingCarPageParam) {
        return CommonResult.data(shoppingCarService.page(shoppingCarPageParam));
    }

    /**
     * 添加直播间购物车表
     *
     * @author lgq
     * @date  2025/03/04 14:17
     */
    @ApiOperation("添加直播间购物车表")
    @CommonLog("添加直播间购物车表")
    @SaCheckPermission("/live/shoppingcar/add")
    @Post
    @Mapping("/live/shoppingcar/add")
    public CommonResult<String> add(ShoppingCarAddParam shoppingCarAddParam) {
        shoppingCarService.add(shoppingCarAddParam);
        return CommonResult.ok();
    }


    @ApiOperation("直播间购物车商品置顶")
    @CommonLog("直播间购物车商品置顶")
    @SaCheckPermission("/live/shoppingcar/top")
    @Post
        @Mapping("/live/shoppingcar/top")
    public CommonResult<String> top(ShoppingCarChangeParam param) {
        if (shoppingCarService.top(param)){
            return CommonResult.ok();
        }
        return CommonResult.error();
    }

    @ApiOperation("直播间购物车商品小黄车提示")
    @CommonLog("直播间购物车商品小黄车提示")
    @SaCheckPermission("/live/shoppingcar/tips")
    @Post
    @Mapping("/live/shoppingcar/tips")
    public CommonResult<String> yellowBikeTips(ShoppingCarChangeParam param) {
        if (shoppingCarService.yellowBikeTips(param)){
            return CommonResult.ok();
        }
        return CommonResult.error();
    }

    /**
     * 编辑直播间购物车表
     *
     * @author lgq
     * @date  2025/03/04 14:17
     */
    @ApiOperation("编辑直播间购物车表")
    @CommonLog("编辑直播间购物车表")
    @SaCheckPermission("/live/shoppingcar/edit")
    @Post
    @Mapping("/live/shoppingcar/edit")
    public CommonResult<String> edit(ShoppingCarEditParam shoppingCarEditParam) {
        shoppingCarService.edit(shoppingCarEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除直播间购物车表
     *
     * @author lgq
     * @date  2025/03/04 14:17
     */
    @ApiOperation("删除直播间购物车表")
    @CommonLog("删除直播间购物车表")
    @SaCheckPermission("/live/shoppingcar/delete")
    @Post
    @Mapping("/live/shoppingcar/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<ShoppingCarIdParam> shoppingCarIdParamList) {
        shoppingCarService.delete(shoppingCarIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取直播间购物车表详情
     *
     * @author lgq
     * @date  2025/03/04 14:17
     */
    @ApiOperation("获取直播间购物车表详情")
    @SaCheckPermission("/live/shoppingcar/detail")
    @Get
    @Mapping("/live/shoppingcar/detail")
    public CommonResult<ShoppingCar> detail(ShoppingCarIdParam shoppingCarIdParam) {
        return CommonResult.data(shoppingCarService.detail(shoppingCarIdParam));
    }
}
