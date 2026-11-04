package vip.xiaonuo.task.modular.record.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.dto.CommonPageParam;
import vip.xiaonuo.task.modular.info.entity.TaskInfo;
import vip.xiaonuo.task.modular.record.entity.TaskRecord;
import vip.xiaonuo.task.modular.record.service.TaskRecordService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/31 18:34
 */
@Slf4j
@Api(tags = "任务记录控制器")
@Controller
@Valid
@Mapping("/task/record")
public class TaskRecordController {

    @Inject
    private TaskRecordService integralTaskService;

    @ApiOperation("任务记录分页")
    @SaCheckPermission("/task/record/page")
    @Get
    @Mapping("/page")
    public CommonResult<Page<TaskRecord>> page(CommonPageParam param) {
        return CommonResult.data(integralTaskService.page(param));
    }

    @ApiOperation("详情")
    @SaCheckPermission("/task/record/detail")
    @Get
    @Mapping("/detail")
    public CommonResult<TaskRecord> detail(String id) {
        return CommonResult.data(integralTaskService.detail(id));
    }
}
