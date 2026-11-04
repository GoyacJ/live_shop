package vip.xiaonuo.mobile.modular.live.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.mobile.modular.live.result.GoodsDetailResult;
import vip.xiaonuo.mobile.modular.live.service.impl.GoodsServiceImpl;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/15 16:21
 * Description: 移动端商品相关接口
 */
@Api(tags = "直播商品控制器")
@Controller
@Valid
@SaIgnore
public class GoodsController {
    @Inject
    private GoodsServiceImpl goodsService;

    @Get
    @Mapping("/mapi/goods/detail")
    @ApiOperation("商品详情")
    public CommonResult<GoodsDetailResult> getGoodsDetail(String id) {
        return CommonResult.data(goodsService.getGoodsDetail(id));
    }
}
