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
package vip.xiaonuo.dev.modular.monitor.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.dev.modular.monitor.result.DevMonitorServerResult;
import vip.xiaonuo.dev.modular.monitor.service.DevMonitorService;


/**
 * 监控控制器
 *
 * @author xuyuxiang
 * @date 2022/6/21 14:57
 **/
@Api(tags = "监控控制器")
@Controller
@Valid
public class DevMonitorController {

    @Inject
    private DevMonitorService devMonitorService;

    /**
     * 获取服务器监控信息
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取服务器监控信息")
    @Get
    @Mapping("/dev/monitor/serverInfo")
    public CommonResult<DevMonitorServerResult> serverInfo() {
        return CommonResult.data(devMonitorService.serverInfo());
    }

    /**
     * 获取服务器网络情况
     *
     * @author diantu
     * @date 2023/7/27
     */
    @ApiOperation("获取服务器网络情况")
    @Get
    @Mapping("/dev/monitor/networkInfo")
    public CommonResult<DevMonitorServerResult> networkInfo() {
        return CommonResult.data(devMonitorService.networkInfo());
    }
}
