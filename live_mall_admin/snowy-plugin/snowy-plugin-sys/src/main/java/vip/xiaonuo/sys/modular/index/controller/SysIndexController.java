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
package vip.xiaonuo.sys.modular.index.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.sys.modular.index.param.*;
import vip.xiaonuo.sys.modular.index.result.*;
import vip.xiaonuo.sys.modular.index.service.SysIndexService;
import org.noear.solon.validation.annotation.NotEmpty;
import java.util.List;

/**
 * 系统首页控制器
 *
 * @author xuyuxiang
 * @date 2022/9/2 10:44
 */
@Api(tags = "系统首页控制器")
@Controller
@Valid
public class SysIndexController {

    @Inject
    private SysIndexService sysIndexService;

    /**
     * 添加当前用户日程
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("添加日程")
    @CommonLog("添加日程")
    @Post
    @Mapping("/sys/index/schedule/add")
    public CommonResult<String> addSchedule(@Validated SysIndexScheduleAddParam sysIndexScheduleAddParam) {
        sysIndexService.addSchedule(sysIndexScheduleAddParam);
        return CommonResult.ok();
    }

    /**
     * 删除日程
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("删除日程")
    @CommonLog("删除日程")
    @Post
    @Mapping("/sys/index/schedule/deleteSchedule")
    public CommonResult<String> deleteSchedule(@Validated @NotEmpty(message = "集合不能为空")
                                               CommonValidList<SysIndexScheduleIdParam> sysIndexScheduleIdParamList) {
        sysIndexService.deleteSchedule(sysIndexScheduleIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取当前用户日程列表
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取日程列表")
    @Get
    @Mapping("/sys/index/schedule/list")
    public CommonResult<List<SysIndexScheduleListResult>> scheduleList(@Validated SysIndexScheduleListParam sysIndexScheduleListParam) {
        return CommonResult.data(sysIndexService.scheduleList(sysIndexScheduleListParam));
    }

    /**
     * 获取当前用户站内信列表
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取当前用户站内信列表")
    @Get
    @Mapping("/sys/index/message/list")
    public CommonResult<List<SysIndexMessageListResult>> messageList(SysIndexMessageListParam sysIndexMessageListParam) {
        return CommonResult.data(sysIndexService.messageList(sysIndexMessageListParam));
    }

    /**
     * 获取站内信详情
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取站内信详情")
    @Get
    @Mapping("/sys/index/message/detail")
    public CommonResult<SysIndexMessageDetailResult> messageDetail(@Validated SysIndexMessageIdParam sysIndexMessageIdParam) {
        return CommonResult.data(sysIndexService.messageDetail(sysIndexMessageIdParam));
    }

    /**
     * 站内信全部标记已读
     *
     * @author diantu
     * @date 2023/7/10
     */
    @ApiOperation("站内信全部标记已读")
    @Post
    @Mapping("/sys/index/message/allMessageMarkRead")
    public CommonResult<String> allMessageMarkRead() {
        sysIndexService.allMessageMarkRead();
        return CommonResult.ok();
    }

    /**
     * 获取当前用户访问日志列表
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取当前用户访问日志列表")
    @Get
    @Mapping("/sys/index/visLog/list")
    public CommonResult<List<SysIndexVisLogListResult>> visLogList() {
        return CommonResult.data(sysIndexService.visLogList());
    }

    /**
     * 获取当前用户操作日志列表
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取当前用户操作日志列表")
    @Get
    @Mapping("/sys/index/opLog/list")
    public CommonResult<List<SysIndexOpLogListResult>> opLogList() {
        return CommonResult.data(sysIndexService.opLogList());
    }

    /**
     * 创建sse连接
     *
     * @author diantu
     * @date 2023/7/10
     **/
//    @ApiOperation("创建sse连接")
//    @Get
//    @Mapping("/dev/message/createSseConnect")
//    public SseEmitter createSseConnect(String clientId){
//        return sysIndexService.createSseConnect(clientId);
//    }


    /**
     * 获取基础系统业务数据
     *
     * @author xuyuxiang、yubaoshan
     * @date 2024/7/18 17:35
     */
    @ApiOperation("获取基础系统业务数据")
    @Get
    @Mapping("/sys/index/bizDataCount")
    public CommonResult<SysBizDataCountResult> getBizDataCount() {
        return CommonResult.data(sysIndexService.getBizDataCount());
    }

    /**
     * 获取运维一览数据
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @ApiOperation("获取运维一览数据")
    @Get
    @Mapping("/sys/index/opDataCount")
    public CommonResult<SysOpDataCountResult> getOpDataCount() {
        return CommonResult.data(sysIndexService.getOpDataCount());
    }

    /**
     * 获取基础工具数据
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    @ApiOperation("获取基础工具数据")
    @Get
    @Mapping("/sys/index/toolDataCount")
    public CommonResult<SysToolDataCountResult> getToolDataCount() {
        return CommonResult.data(sysIndexService.getToolDataCount());
    }
}
