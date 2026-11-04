package vip.xiaonuo.mobile.modular.content.action.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.content.api.dto.ContentUserActionDTO;
import vip.xiaonuo.content.api.dto.ContentWatchProgressDTO;
import vip.xiaonuo.mobile.modular.content.action.service.ContentUserActionService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 13:32
 */
@Api(tags = "内容用户通用行为控制器")
@Controller
@Valid
@SaIgnore
@Mapping("mapi/content/user-action")
public class ContentUserActionController {

    @Inject
    private ContentUserActionService contentUserActionService;

    @ApiOperation("用户通用行为")
    @Post
    @Mapping("action")
    public CommonResult<Void> action(ContentUserActionDTO dto) {
        contentUserActionService.action(dto);
        return CommonResult.ok();
    }

    @ApiOperation("用户观看")
    @Post
    @Mapping("visit")
    public CommonResult<Void> visit(ContentWatchProgressDTO dto) {
        contentUserActionService.visit(dto);
        return CommonResult.ok();
    }
}
