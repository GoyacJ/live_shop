package vip.xiaonuo.content.modular.video.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.content.modular.video.dto.ContentVideoDTO;
import vip.xiaonuo.content.modular.video.entity.ContentVideo;
import vip.xiaonuo.content.modular.video.param.*;
import vip.xiaonuo.content.modular.video.service.ContentVideoService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 13:30
 */
@Slf4j
@Api(tags = "视频控制器")
@Controller
@Valid
@Mapping("/content/video")
public class ContentVideoController {

    @Inject
    private ContentVideoService contentVideoService;

    @ApiOperation("分页")
    @SaCheckPermission("/content/video/page")
    @Get
    @Mapping("/page")
    public CommonResult<Page<ContentVideoDTO>> page(ContentVideoPageParam param) {
        return CommonResult.data(contentVideoService.page(param));
    }

    @ApiOperation("详情")
    @SaCheckPermission("/content/video/detail")
    @Get
    @Mapping("/detail")
    public CommonResult<ContentVideo> detail(String id) {
        return CommonResult.data(contentVideoService.detail(id));
    }

    @ApiOperation("新增")
    @CommonLog("新增视频")
    @SaCheckPermission("/content/video/add")
    @Post
    @Mapping("/add")
    public CommonResult<Void> add(ContentVideoAddParam param) {
        contentVideoService.add(param);
        return CommonResult.ok();
    }

    @ApiOperation("批量新增")
    @CommonLog("批量新增视频")
    @SaCheckPermission("/content/video/batch-add")
    @Post
    @Mapping("/batch-add")
    public CommonResult<Void> batchAdd(CommonValidList<ContentVideoAddParam> params) {
        contentVideoService.batchAdd(params);
        return CommonResult.ok();
    }

    @ApiOperation("编辑")
    @CommonLog("编辑视频")
    @SaCheckPermission("/content/video/edit")
    @Post
    @Mapping("/edit")
    public CommonResult<Void> edit(ContentVideoEditParam param) {
        contentVideoService.edit(param);
        return CommonResult.ok();
    }

    @ApiOperation("删除")
    @CommonLog("删除视频")
    @SaCheckPermission("/content/video/delete")
    @Post
    @Mapping("/delete")
    public CommonResult<Void> delete(@NotEmpty(message = "集合不能为空")
                                           CommonValidList<ContentVideoIdParam> param) {
        contentVideoService.delete(param);
        return CommonResult.ok();
    }

    @ApiOperation("更改视频状态")
    @CommonLog("更改视频状态")
    @SaCheckPermission("/content/video/change-status")
    @Post
    @Mapping("/change-status")
    public CommonResult<Void> changeStatus(ContentVideoChangeStatusParam param) {
        contentVideoService.changeStatus(param);
        return CommonResult.ok();
    }

}
