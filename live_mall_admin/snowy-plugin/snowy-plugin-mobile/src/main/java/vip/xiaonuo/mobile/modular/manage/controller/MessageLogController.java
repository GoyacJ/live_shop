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
package vip.xiaonuo.mobile.modular.manage.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.manage.modular.messagelog.param.MessageLogAddParam;
import vip.xiaonuo.manage.modular.messagelog.param.MessageLogPageParam;
import vip.xiaonuo.mobile.modular.manage.result.MessageLogResult;
import vip.xiaonuo.mobile.modular.manage.service.MessageLogService;

import java.util.List;

/**
 * 消息记录控制器
 *
 */
@Api(tags = "消息记录控制器")
@Controller
@Valid
public class MessageLogController {

    @Inject
    private MessageLogService messageLogService;

    /**
     * 获取反馈意见列表分页
     *
     */
    @SaIgnore
    @ApiOperation("获取消息记录列表分页")
    @Get
    @Mapping("/mapi/auth/getmessagelogpage")
    public CommonResult<Page<MessageLogResult>> page(MessageLogPageParam messageLogPageParam) {
        return CommonResult.data(messageLogService.page(messageLogPageParam));
    }


    /**
     * 添加消息记录
     *
     */
    @SaIgnore
    @ApiOperation("添加消息记录")
    @Post
    @Mapping("/mapi/auth/messagelogadd")
    public CommonResult<String> add(@Body @Validated MessageLogAddParam messageLogAddParam) {
        messageLogService.add(messageLogAddParam);
        return CommonResult.ok();
    }
}
