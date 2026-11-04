/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.live.modular.tasksheet.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.live.modular.tasksheet.entity.TaskSheet;
import vip.xiaonuo.live.modular.tasksheet.param.TaskSheetAddParam;
import vip.xiaonuo.live.modular.tasksheet.param.TaskSheetEditParam;
import vip.xiaonuo.live.modular.tasksheet.param.TaskSheetIdParam;
import vip.xiaonuo.live.modular.tasksheet.param.TaskSheetPageParam;
import vip.xiaonuo.live.modular.tasksheet.service.TaskSheetService;

/**
 * 任务表控制器
 *
 * @author lgq
 * @date  2025/03/04 14:20
 */
@Api(tags = "任务表控制器")
@Controller
@Valid
public class TaskSheetController {

    @Inject
    private TaskSheetService taskSheetService;

    /**
     * 获取任务表分页
     *
     * @author lgq
     * @date  2025/03/04 14:20
     */
    @ApiOperation("获取任务表分页")
    @SaCheckPermission("/live/tasksheet/page")
    @Get
    @Mapping("/live/tasksheet/page")
    public CommonResult<Page<TaskSheet>> page(TaskSheetPageParam taskSheetPageParam) {
        return CommonResult.data(taskSheetService.page(taskSheetPageParam));
    }

    /**
     * 添加任务表
     *
     * @author lgq
     * @date  2025/03/04 14:20
     */
    @ApiOperation("添加任务表")
    @CommonLog("添加任务表")
    @SaCheckPermission("/live/tasksheet/add")
    @Post
    @Mapping("/live/tasksheet/add")
    public CommonResult<String> add(TaskSheetAddParam taskSheetAddParam) {
        taskSheetService.add(taskSheetAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑任务表
     *
     * @author lgq
     * @date  2025/03/04 14:20
     */
    @ApiOperation("编辑任务表")
    @CommonLog("编辑任务表")
    @SaCheckPermission("/live/tasksheet/edit")
    @Post
    @Mapping("/live/tasksheet/edit")
    public CommonResult<String> edit(TaskSheetEditParam taskSheetEditParam) {
        taskSheetService.edit(taskSheetEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除任务表
     *
     * @author lgq
     * @date  2025/03/04 14:20
     */
    @ApiOperation("删除任务表")
    @CommonLog("删除任务表")
    @SaCheckPermission("/live/tasksheet/delete")
    @Post
    @Mapping("/live/tasksheet/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<TaskSheetIdParam> taskSheetIdParamList) {
        taskSheetService.delete(taskSheetIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取任务表详情
     *
     * @author lgq
     * @date  2025/03/04 14:20
     */
    @ApiOperation("获取任务表详情")
    @SaCheckPermission("/live/tasksheet/detail")
    @Get
    @Mapping("/live/tasksheet/detail")
    public CommonResult<TaskSheet> detail(TaskSheetIdParam taskSheetIdParam) {
        return CommonResult.data(taskSheetService.detail(taskSheetIdParam));
    }
}
