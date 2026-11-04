package vip.xiaonuo.mobile.modular.content.shortplay.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.content.api.dto.*;
import vip.xiaonuo.mobile.modular.content.shortplay.service.ShortPlayService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/14 15:17
 */
@Api(tags = "短剧控制器")
@Controller
@Valid
@SaIgnore
@Mapping("mapi/content/short-play")
public class ShortPlayController {

    @Inject
    private ShortPlayService shortPlayService;

    @ApiOperation("短剧分页")
    @Get
    @Mapping("/page")
    public CommonResult<Page<ShortPlayByActionDTO>> page(ShortPlayByActionPageDTO dto) {
        return CommonResult.data(shortPlayService.page(dto));
    }

    @ApiOperation("剧集分页")
    @Get
    @Mapping("/episode/page")
    public CommonResult<Page<ShortPlayEpisodeByActionDTO>> episodePage(ShortPlayEpisodeByActionPageDTO dto) {
        return CommonResult.data(shortPlayService.episodePage(dto));
    }

    @ApiOperation("短剧首页推荐")
    @Get
    @Mapping("/index")
    public CommonResult<Page<ShortPlayByActionDTO>> index(ShortPlayByActionPageDTO dto) {
        return CommonResult.data(shortPlayService.index(dto));
    }

    @ApiOperation("追剧")
    @Post
    @Mapping("/follow")
    public CommonResult<Void> follow(ShopPlayFollowDTO dto) {
        shortPlayService.follow(dto);
        return CommonResult.ok();
    }
}
