package vip.xiaonuo.mobile.modular.advertisement.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.mobile.modular.advertisement.dto.AdvertisementVisitDTO;
import vip.xiaonuo.mobile.modular.advertisement.service.AdvertisementService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 19:36
 */
@SaIgnore
@Api(tags = "广告控制器")
@Controller
@Valid
@Mapping("/mapi/advertisement")
public class AdvertisementController {

    @Inject
    private AdvertisementService advertisementService;

    @ApiOperation("看广告")
    @Post
    @Mapping("/visit")
    public CommonResult<Void> visit(@Validated AdvertisementVisitDTO dto) {
        advertisementService.visit(dto);
        return CommonResult.ok();
    }
}
