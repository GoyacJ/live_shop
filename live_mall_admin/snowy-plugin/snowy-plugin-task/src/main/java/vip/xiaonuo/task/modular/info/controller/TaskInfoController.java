package vip.xiaonuo.task.modular.info.controller;

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
import vip.xiaonuo.task.modular.info.entity.TaskInfo;
import vip.xiaonuo.task.modular.info.params.*;
import vip.xiaonuo.task.modular.info.service.TaskInfoService;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 13:56
 */
@Slf4j
@Api(tags = "任务管理控制器")
@Controller
@Valid
@Mapping("/task/info")
public class TaskInfoController {

    @Inject
    private TaskInfoService integralTaskService;

    @ApiOperation("推广订单管理分页")
    @SaCheckPermission("/task/info/page")
    @Get
    @Mapping("/page")
    public CommonResult<Page<TaskInfo>> page(TaskInfoPageParam param) {
        return CommonResult.data(integralTaskService.page(param));
    }

    @ApiOperation("详情")
    @SaCheckPermission("/task/info/detail")
    @Get
    @Mapping("/detail")
    public CommonResult<TaskInfo> detail(String id) {
        return CommonResult.data(integralTaskService.detail(id));
    }

    @ApiOperation("新增")
    @CommonLog("新增积分任务")
    @SaCheckPermission("/task/info/add")
    @Post
    @Mapping("/add")
    public CommonResult<Void> add(TaskInfoAddParam param) {
        integralTaskService.add(param);
        return CommonResult.ok();
    }

    @ApiOperation("编辑")
    @CommonLog("编辑积分任务")
    @SaCheckPermission("/task/info/edit")
    @Post
    @Mapping("/edit")
    public CommonResult<Void> edit(TaskInfoEditParam param) {
        integralTaskService.edit(param);
        return CommonResult.ok();
    }

    @ApiOperation("删除")
    @CommonLog("删除积分任务")
    @SaCheckPermission("/task/info/delete")
    @Post
    @Mapping("/delete")
    public CommonResult<Void> delete(@NotEmpty(message = "集合不能为空")
                                     CommonValidList<TaskInfoIdParam> param) {
        integralTaskService.delete(param);
        return CommonResult.ok();
    }

    @ApiOperation("更改积分任务状态")
    @CommonLog("更改积分任务状态")
    @SaCheckPermission("/task/info/change-status")
    @Post
    @Mapping("/change-status")
    public CommonResult<Void> changeStatus(TaskInfoChangeStatusParam param) {
        integralTaskService.changeStatus(param);
        return CommonResult.ok();
    }
}
