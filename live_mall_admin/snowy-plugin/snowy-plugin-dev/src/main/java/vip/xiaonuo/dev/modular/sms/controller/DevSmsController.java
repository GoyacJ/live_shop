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
package vip.xiaonuo.dev.modular.sms.controller;

import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.dev.modular.sms.entity.DevSms;
import vip.xiaonuo.dev.modular.sms.param.DevSmsIdParam;
import vip.xiaonuo.dev.modular.sms.param.DevSmsPageParam;
import vip.xiaonuo.dev.modular.sms.param.DevSmsSendAliyunParam;
import vip.xiaonuo.dev.modular.sms.param.DevSmsSendTencentParam;
import vip.xiaonuo.dev.modular.sms.service.DevSmsService;

import org.noear.solon.validation.annotation.NotEmpty;

/**
 * 短信控制器
 *
 * @author xuyuxiang
 * @date 2022/2/23 18:26
 **/
@Api(tags = "短信控制器")
@Controller
@Valid
public class DevSmsController {

    @Inject
    private DevSmsService devSmsService;

    /**
     * 发送短信——阿里云
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("发送阿里云短信")
    @CommonLog("发送阿里云短信")
    @Post
    @Mapping("/dev/sms/sendAliyun")
    public CommonResult<String> sendAliyun(@Validated DevSmsSendAliyunParam devSmsSendAliyunParam) {
        devSmsService.sendAliyun(devSmsSendAliyunParam);
        return CommonResult.ok();
    }

    /**
     * 发送短信——腾讯云
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:47
     */
    @ApiOperation("发送腾讯云短信")
    @CommonLog("发送腾讯云短信")
    @Post@Mapping("/dev/sms/sendTencent")
    public CommonResult<String> sendTencent(@Validated DevSmsSendTencentParam devSmsSendTencentParam) {
        devSmsService.sendTencent(devSmsSendTencentParam);
        return CommonResult.ok();
    }

    /**
     * 获取短信分页
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取短信分页")
    @Get
    @Mapping("/dev/sms/page")
    public CommonResult<Page<DevSms>> page(DevSmsPageParam devSmsPageParam) {
        return CommonResult.data(devSmsService.page(devSmsPageParam));
    }

    /**
     * 删除短信
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("删除短信")
    @CommonLog("删除短信")
    @Post
    @Mapping("/dev/sms/delete")
    public CommonResult<String> delete(@Validated @NotEmpty(message = "集合不能为空")
                                               CommonValidList<DevSmsIdParam> devSmsIdParamList) {
        devSmsService.delete(devSmsIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取短信详情
     *
     * @author xuyuxiang
     * @date 2022/4/24 20:00
     */
    @ApiOperation("获取短信详情")
    @Get
    @Mapping("/dev/sms/detail")
    public CommonResult<DevSms> detail(@Validated DevSmsIdParam devSmsIdParam) {
        return CommonResult.data(devSmsService.detail(devSmsIdParam));
    }
}
