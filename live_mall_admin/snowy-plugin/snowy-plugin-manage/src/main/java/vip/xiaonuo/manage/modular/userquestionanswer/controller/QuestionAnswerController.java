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
package vip.xiaonuo.manage.modular.userquestionanswer.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
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
import vip.xiaonuo.manage.modular.feedback.entity.BizFeedback;
import vip.xiaonuo.manage.modular.feedback.param.*;
import vip.xiaonuo.manage.modular.feedback.service.BizFeedBackService;
import vip.xiaonuo.manage.modular.userquestionanswer.entity.UserQuestionAnswer;
import vip.xiaonuo.manage.modular.userquestionanswer.param.QuestionAnswerAddParam;
import vip.xiaonuo.manage.modular.userquestionanswer.param.QuestionAnswerEditParam;
import vip.xiaonuo.manage.modular.userquestionanswer.param.QuestionAnswerIdParam;
import vip.xiaonuo.manage.modular.userquestionanswer.param.QuestionAnswerPageParam;
import vip.xiaonuo.manage.modular.userquestionanswer.service.QuestionAnswerService;

import java.util.List;

/**
 * 反馈意见控制器
 *
 */
@Api(tags = "问题答案控制器")
@Controller
@Valid
public class QuestionAnswerController {

    @Inject
    private QuestionAnswerService questionAnswerService;

    /**
     * 获取反馈意见列表分页
     *
     */
    @ApiOperation("获取问题答案列表分页")
    @SaCheckPermission("/manage/questionanswer/page")
    @Get
    @Mapping("/manage/questionanswer/page")
    public CommonResult<Page<UserQuestionAnswer>> page(QuestionAnswerPageParam questionAnswerPageParam) {
        return CommonResult.data(questionAnswerService.page(questionAnswerPageParam));
    }

    /**
     * 编辑反馈意见
     *
     */
    @ApiOperation("编辑反馈意见")
    @CommonLog("编辑反馈意见")
    @SaCheckPermission("/manage/questionanswer/edit")
    @Post
    @Mapping("/manage/questionanswer/edit")
    public CommonResult<String> edit(QuestionAnswerEditParam questionAnswerEditParam) {
        questionAnswerService.edit(questionAnswerEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除反馈意见
     *
     */
    @ApiOperation("删除反馈意见")
    @CommonLog("删除反馈意见")
    @SaCheckPermission("/manage/questionanswer/delete")
    @Post
    @Mapping("/manage/questionanswer/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                       CommonValidList<QuestionAnswerIdParam> questionAnswerIdParamList) {
        questionAnswerService.delete(questionAnswerIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取反馈意见详情
     *
     */
    @ApiOperation("获取反馈意见详情")
    @SaCheckPermission("/manage/questionanswer/detail")
    @Get
    @Mapping("/manage/questionanswer/detail")
    public CommonResult<UserQuestionAnswer> detail(QuestionAnswerIdParam questionAnswerIdParam) {
        return CommonResult.data(questionAnswerService.detail(questionAnswerIdParam));
    }

    /**
     * 添加用户组
     *
     */
    @ApiOperation("添加用户组")
    @CommonLog("添加用户组")
    @SaCheckPermission("/manage/questionanswer/add")
    @Post
    @Mapping("/manage/questionanswer/add")
    public CommonResult<String> add(@Body @Validated QuestionAnswerAddParam questionAnswerAddParam) {
        questionAnswerService.add(questionAnswerAddParam);
        return CommonResult.ok();
    }

}
