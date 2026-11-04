package vip.xiaonuo.mobile.modular.content.video.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.content.api.dto.VideoByActionDTO;
import vip.xiaonuo.content.api.dto.VideoByActionPageDTO;
import vip.xiaonuo.mobile.modular.content.video.service.VideoService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/6 14:28
 */
@Api(tags = "视频控制器")
@Controller
@Valid
@SaIgnore
@Mapping("mapi/content/video")
public class VideoController {

    @Inject
    private VideoService videoService;

    @ApiOperation("视频分页")
    @Get
    @Mapping("/page")
    public CommonResult<Page<VideoByActionDTO>> page(VideoByActionPageDTO dto) {
        return CommonResult.data(videoService.page(dto));
    }
}
