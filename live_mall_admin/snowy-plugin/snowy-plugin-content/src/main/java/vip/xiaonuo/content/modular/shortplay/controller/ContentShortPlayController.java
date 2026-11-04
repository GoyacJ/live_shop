package vip.xiaonuo.content.modular.shortplay.controller;

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
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlay;
import vip.xiaonuo.content.modular.shortplay.entity.ContentShortPlayEpisode;
import vip.xiaonuo.content.modular.shortplay.param.*;
import vip.xiaonuo.content.modular.shortplay.service.ContentShortPlayService;
import vip.xiaonuo.content.modular.video.dto.ContentShortPlayDTO;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 13:33
 */
@Slf4j
@Api(tags = "视频控制器")
@Controller
@Valid
@Mapping("/content/short-play")
public class ContentShortPlayController {

    @Inject
    private ContentShortPlayService contentShortPlayService;

    @ApiOperation("分页")
    @SaCheckPermission("/content/short-play/page")
    @Get
    @Mapping("/page")
    public CommonResult<Page<ContentShortPlayDTO>> page(ContentShortPlayPageParam param) {
        return CommonResult.data(contentShortPlayService.page(param));
    }

    @ApiOperation("详情")
    @SaCheckPermission("/content/short-play/detail")
    @Get
    @Mapping("/detail")
    public CommonResult<ContentShortPlay> detail(String id) {
        return CommonResult.data(contentShortPlayService.detail(id));
    }

    @ApiOperation("新增")
    @CommonLog("新增短剧")
    @SaCheckPermission("/content/short-play/add")
    @Post
    @Mapping("/add")
    public CommonResult<Void> add(ContentShortPlayAddParam param) {
        contentShortPlayService.add(param);
        return CommonResult.ok();
    }

    @ApiOperation("编辑")
    @CommonLog("编辑短剧")
    @SaCheckPermission("/content/short-play/edit")
    @Post
    @Mapping("/edit")
    public CommonResult<Void> edit(ContentShortPlayEditParam param) {
        contentShortPlayService.edit(param);
        return CommonResult.ok();
    }

    @ApiOperation("删除")
    @CommonLog("删除短剧")
    @SaCheckPermission("/content/short-play/delete")
    @Post
    @Mapping("/delete")
    public CommonResult<Void> delete(@NotEmpty(message = "集合不能为空")
                                     CommonValidList<ContentShortPlayIdParam> param) {
        contentShortPlayService.delete(param);
        return CommonResult.ok();
    }

    @ApiOperation("更改短剧状态")
    @CommonLog("更改短剧状态")
    @SaCheckPermission("/content/short-play/change-status")
    @Post
    @Mapping("/change-status")
    public CommonResult<Void> changeStatus(ContentShortPlayChangeStatusParam param) {
        contentShortPlayService.changeStatus(param);
        return CommonResult.ok();
    }

    @ApiOperation("剧集分页")
    @SaCheckPermission("/content/short-play/episode/page")
    @Get
    @Mapping("/episode/page")
    public CommonResult<Page<ContentShortPlayEpisode>> episodePage(ContentShortPlayEpisodePageParam param) {
        return CommonResult.data(contentShortPlayService.episodePage(param));
    }

    @ApiOperation("剧集详情")
    @SaCheckPermission("/content/short-play/episode/detail")
    @Get
    @Mapping("/episode/detail")
    public CommonResult<ContentShortPlayEpisode> episodeDetail(String id) {
        return CommonResult.data(contentShortPlayService.episodeDetail(id));
    }

    @ApiOperation("剧集新增")
    @CommonLog("新增剧集")
    @SaCheckPermission("/content/short-play/episode/add")
    @Post
    @Mapping("/episode/add")
    public CommonResult<Void> episodeAdd(ContentShortPlayEpisodeAddParam param) {
        contentShortPlayService.episodeAdd(param);
        return CommonResult.ok();
    }

    @ApiOperation("剧集新增")
    @CommonLog("新增剧集")
    @SaCheckPermission("/content/short-play/episode/batch-add")
    @Post
    @Mapping("/episode/batch-add")
    public CommonResult<Void> episodeBatchAdd(CommonValidList<ContentShortPlayEpisodeAddParam> params) {
        contentShortPlayService.episodeBatchAdd(params);
        return CommonResult.ok();
    }

    @ApiOperation("编辑剧集")
    @CommonLog("编辑剧集")
    @SaCheckPermission("/content/short-play/episode/edit")
    @Post
    @Mapping("/episode/edit")
    public CommonResult<Void> episodeEdit(ContentShortPlayEpisodeEditParam param) {
        contentShortPlayService.episodeEdit(param);
        return CommonResult.ok();
    }

    @ApiOperation("删除剧集")
    @CommonLog("删除剧集")
    @SaCheckPermission("/content/short-play/episode/delete")
    @Post
    @Mapping("/episode/delete")
    public CommonResult<Void> episodeDelete(@NotEmpty(message = "集合不能为空")
                                     CommonValidList<ContentShortPlayEpisodeIdParam> param) {
        contentShortPlayService.episodeDelete(param);
        return CommonResult.ok();
    }

}
