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
package vip.xiaonuo.dev.modular.job.controller;

import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.dev.modular.job.entity.DevJob;
import vip.xiaonuo.dev.modular.job.param.*;
import vip.xiaonuo.dev.modular.job.service.DevJobService;

import java.util.List;

/**
 * 定时任务控制器
 *
 * @author xuyuxiang
 * @date 2022/8/5 10:48
 **/
@Api(tags = "定时任务控制器")
@Controller
@Valid
public class DevJobController {

    @Inject
    private DevJobService devJobService;

    /**
     * 获取定时任务分页
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取定时任务分页")
    @Get
    @Mapping("/dev/job/page")
    public CommonResult<Page<DevJob>> page(DevJobPageParam devJobPageParam) {
        return CommonResult.data(devJobService.page(devJobPageParam));
    }

    /**
     * 获取定时任务列表
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取定时任务列表")
    @Get
    @Mapping("/dev/job/list")
    public CommonResult<List<DevJob>> list(DevJobListParam devJobListParam) {
        return CommonResult.data(devJobService.list(devJobListParam));
    }

    /**
     * 添加定时任务
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("添加定时任务")
    @CommonLog("添加定时任务")
    @Post
    @Mapping("/dev/job/add")
    public CommonResult<String> add(@Validated DevJobAddParam devJobAddParam) {
        devJobService.add(devJobAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑定时任务
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("编辑定时任务")
    @CommonLog("编辑定时任务")
    @Post
    @Mapping("/dev/job/edit")
    public CommonResult<String> edit(@Validated DevJobEditParam devJobEditParam) {
        devJobService.edit(devJobEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除定时任务
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("删除定时任务")
    @CommonLog("删除定时任务")
    @Post
    @Mapping("/dev/job/delete")
    public CommonResult<String> delete(@Validated @NotEmpty(message = "集合不能为空")
                                               CommonValidList<DevJobIdParam> devJobIdParamList) {
        devJobService.delete(devJobIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取定时任务详情
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取定时任务详情")
    @Get
    @Mapping("/dev/job/detail")
    public CommonResult<DevJob> detail(@Validated DevJobIdParam devJobIdParam) {
        return CommonResult.data(devJobService.detail(devJobIdParam));
    }

    /**
     * 停止定时任务
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @ApiOperation("停止定时任务")
    @CommonLog("停止定时任务")
    @Post
    @Mapping("/dev/job/stopJob")
    public CommonResult<String> stopJob(DevJobIdParam devJobIdParam) {
        devJobService.stopJob(devJobIdParam);
        return CommonResult.ok();
    }

    /**
     * 运行定时任务
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @ApiOperation("运行定时任务")
    @CommonLog("运行定时任务")
    @Post
    @Mapping("/dev/job/runJob")
    public CommonResult<String> runJob(@Validated DevJobIdParam devJobIdParam) {
        devJobService.runJob(devJobIdParam);
        return CommonResult.ok();
    }

    /**
     * 立即运行定时任务
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @ApiOperation("立即运行定时任务")
    @CommonLog("立即运行定时任务")
    @Post
    @Mapping("/dev/job/runJobNow")
    public CommonResult<String> runJobNow(@Validated DevJobIdParam devJobIdParam) {
        devJobService.runJobNow(devJobIdParam);
        return CommonResult.ok();
    }

    /**
     * 获取定时任务类
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取定时任务类")
    @Get
    @Mapping("/dev/job/getActionClass")
    public CommonResult<List<String>> getActionClass() {
        return CommonResult.data(devJobService.getActionClass());
    }
}
