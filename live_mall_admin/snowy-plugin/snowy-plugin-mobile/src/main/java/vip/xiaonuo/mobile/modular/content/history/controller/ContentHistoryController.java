package vip.xiaonuo.mobile.modular.content.history.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.content.api.dto.ContentUserActionDTO;
import vip.xiaonuo.content.api.dto.ContentWatchHistoryDTO;
import vip.xiaonuo.content.api.dto.ContentWatchHistoryPageDTO;
import vip.xiaonuo.mobile.modular.content.history.service.ContentHistoryService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 14:44
 */
@Api(tags = "内容历史控制器")
@Controller
@Valid
@SaIgnore
@Mapping("mapi/content/history")
public class ContentHistoryController {

    @Inject
    private ContentHistoryService contentHistoryService;

    @ApiOperation("历史记录")
    @Get
    @Mapping("record")
    public CommonResult<Page<ContentWatchHistoryDTO>> record(ContentWatchHistoryPageDTO dto) {
        return CommonResult.data(contentHistoryService.record(dto));
    }

    @ApiOperation("删除历史记录")
    @Post
    @Mapping("delete-record")
    public CommonResult<Void> deleteRecord(CommonIdParam dto) {
        contentHistoryService.deleteRecord(dto);
        return CommonResult.ok();
    }
}
