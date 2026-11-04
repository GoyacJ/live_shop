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


import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.manage.modular.feedback.param.BizFeedbackAddParam;
import vip.xiaonuo.manage.modular.feedback.param.BizFeedbackMobileParam;
import vip.xiaonuo.mobile.modular.manage.result.FeedbackResult;
import vip.xiaonuo.mobile.modular.manage.service.FeedBackService;

import java.util.List;

/**
 * 反馈意见控制器
 *
 */
@Api(tags = "反馈意见控制器")
@Controller
@SaIgnore
@Valid
public class FeedBackController {

    @Inject
    private FeedBackService bizFeedBackService;

    /**
     * 添加用户组
     *
     */
    @ApiOperation("添加用户组")
    /*@CommonLog("添加用户组")*/
    @Post
    @Mapping("/mapi/auth/feedbackAdd")
    public CommonResult<String> add(String content) {
        if (StrUtil.isBlank(content)) {
            return CommonResult.error("反馈内容不能为空");
        }
        bizFeedBackService.add(content);
        return CommonResult.ok();
    }

    @Get
    @Mapping("/mapi/auth/feedbackList")
    public CommonResult<List<FeedbackResult>> detailByMobile(){
        return CommonResult.data(bizFeedBackService.feedbackList());
    }

    /**
     * 根据手机号获取反馈意见
     *
     */
    @ApiOperation("根据手机号获取反馈意见")
    @Get
    @Mapping("/mapi/auth/getFeedBackListBymobile")
    public CommonResult<List<FeedbackResult>> detailByMobile(@Validated BizFeedbackMobileParam bizFeedbackMobileParam){
        return CommonResult.data(bizFeedBackService.detailByMobile(bizFeedbackMobileParam));
    }
}
