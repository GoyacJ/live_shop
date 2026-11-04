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
package vip.xiaonuo.manage.modular.feedback.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
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
import vip.xiaonuo.manage.modular.userinfo.entity.UserInfo;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoEditParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoIdParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoPageParam;

import java.util.List;

/**
 * 反馈意见控制器
 *
 */
@Api(tags = "反馈意见控制器")
@Controller
@Valid
public class BizFeedBackController {

    @Inject
    private BizFeedBackService bizFeedBackService;

    /**
     * 获取反馈意见列表分页
     *
     */
    @ApiOperation("获取反馈意见列表分页")
    @SaCheckPermission("/manage/feedback/page")
    @Get
    @Mapping("/manage/feedback/page")
    public CommonResult<Page<BizFeedback>> page(BizFeedbackPageParam bizFeedbackPageParam) {
        return CommonResult.data(bizFeedBackService.page(bizFeedbackPageParam));
    }

    /**
     * 编辑反馈意见
     *
     */
    @ApiOperation("编辑反馈意见")
    @CommonLog("编辑反馈意见")
    @SaCheckPermission("/manage/feedback/edit")
    @Post
    @Mapping("/manage/feedback/edit")
    public CommonResult<String> edit(BizFeedbackEditParam bizFeedbackEditParam) {
        bizFeedBackService.edit(bizFeedbackEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除反馈意见
     *
     */
    @ApiOperation("删除反馈意见")
    @CommonLog("删除反馈意见")
    @SaCheckPermission("/manage/feedback/delete")
    @Post
    @Mapping("/manage/feedback/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                       CommonValidList<BizFeedbackIdParam> bizFeedbackIdParamList) {
        bizFeedBackService.delete(bizFeedbackIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取反馈意见详情
     *
     */
    @ApiOperation("获取反馈意见详情")
    @SaCheckPermission("/manage/feedback/detail")
    @Get
    @Mapping("/manage/feedback/detail")
    public CommonResult<BizFeedback> detail(BizFeedbackIdParam bizFeedbackIdParam) {
        return CommonResult.data(bizFeedBackService.detail(bizFeedbackIdParam));
    }

    /**
     * 添加用户组
     *
     */
    @ApiOperation("添加用户组")
    @CommonLog("添加用户组")
    @SaCheckPermission("/manage/feedback/add")
    @Post
    @Mapping("/manage/feedback/add")
    public CommonResult<String> add(@Body @Validated BizFeedbackAddParam bizFeedbackAddParam) {
        bizFeedBackService.add(bizFeedbackAddParam);
        return CommonResult.ok();
    }

    /**
     * 根据手机号获取反馈意见
     *
     */
    @ApiOperation("根据手机号获取反馈意见")
    @SaCheckPermission("/manage/feedback/detailbymobile")
    @Get
    @Mapping("/manage/feedback/detailbymobile")
    public CommonResult<List<BizFeedback>> detailByMobile(@Validated BizFeedbackMobileParam bizFeedbackMobileParam) {
        return CommonResult.data(bizFeedBackService.detailByMobile(bizFeedbackMobileParam));
    }
}
