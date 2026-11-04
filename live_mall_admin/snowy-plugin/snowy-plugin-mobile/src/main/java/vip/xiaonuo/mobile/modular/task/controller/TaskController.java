package vip.xiaonuo.mobile.modular.task.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotNull;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.mobile.modular.task.service.TaskService;
import vip.xiaonuo.mobile.modular.task.vo.TaskInfoVO;

import java.util.List;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/6 11:03
 */
@Api(tags = "任务控制器")
@Controller
@Valid
@SaIgnore
@Mapping("mapi/task")
public class TaskController {

    @Inject
    private TaskService taskService;

    @ApiOperation("获取任务列表")
    @Get
    @Mapping("/list")
    public CommonResult<List<TaskInfoVO>> getTaskListByUserId(@NotNull String userId) {
        return CommonResult.data(taskService.getTaskListByUserId(userId));
    }

    @ApiOperation("签到")
    @Post
    @Mapping("/sign-in")
    public CommonResult<Void> signInByDay(@NotNull String userId) {
        taskService.signInByDay(userId);
        return CommonResult.ok();
    }
}
