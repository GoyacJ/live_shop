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
package vip.xiaonuo.manage.modular.useraccount.controller;


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
import vip.xiaonuo.manage.modular.useraccount.entity.UserAccount;
import vip.xiaonuo.manage.modular.useraccount.param.*;
import vip.xiaonuo.manage.modular.useraccount.service.UserAccountService;

import java.util.List;

/**
 * 用户账号控制器
 *
 */
@Api(tags = "用户账号控制器")
@Controller
@Valid
public class UserAccountController {

    @Inject
    private UserAccountService userAccountService;

    /**
     * 获取用户账号列表分页
     *
     */
    @ApiOperation("获取反馈意见列表分页")
    @SaCheckPermission("/manage/useraccount/page")
    @Get
    @Mapping("/manage/useraccount/page")
    public CommonResult<Page<UserAccount>> page(UserAccountPageParam userAccountPageParam) {
        return CommonResult.data(userAccountService.page(userAccountPageParam));
    }

    /**
     * 编辑 用户账号
     *
     */
    @ApiOperation("编辑反馈意见")
    @CommonLog("编辑反馈意见")
    @SaCheckPermission("/manage/useraccount/edit")
    @Post
    @Mapping("/manage/useraccount/edit")
    public CommonResult<String> edit(UserAccountEditParam userAccountEditParam) {
        userAccountService.edit(userAccountEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除 用户账号
     *
     */
    @ApiOperation("删除反馈意见")
    @CommonLog("删除反馈意见")
    @SaCheckPermission("/manage/useraccount/delete")
    @Post
    @Mapping("/manage/useraccount/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                       CommonValidList<UserAccountIdParam> userAccountIdParamList) {
        userAccountService.delete(userAccountIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取 用户账号详情
     *
     */
    @ApiOperation("获取反馈意见详情")
    @SaCheckPermission("/manage/useraccount/detail")
    @Get
    @Mapping("/manage/useraccount/detail")
    public CommonResult<UserAccount> detail(UserAccountIdParam userAccountIdParam) {
        return CommonResult.data(userAccountService.detail(userAccountIdParam));
    }

    /**
     * 添加 用户账号
     *
     */
    @ApiOperation("添加用户组")
    @CommonLog("添加用户组")
    @SaCheckPermission("/manage/useraccount/add")
    @Post
    @Mapping("/manage/useraccount/add")
    public CommonResult<String> add(@Body @Validated UserAccountAddParam userAccountAddParam) {
        userAccountService.add(userAccountAddParam);
        return CommonResult.ok();
    }

    /**
     * 根据用户获取用户账号list
     *
     */
    @ApiOperation("根据用户获取用户账号list")
    @SaCheckPermission("/manage/useraccount/detailbyuser")
    @Get
    @Mapping("/manage/useraccount/detailbyuser")
    public CommonResult<List<UserAccount>> getDetailByUserId(UserAccountUserIdParam userAccountUserIdParam) {
        return CommonResult.data(userAccountService.getDetailByUserId(userAccountUserIdParam));
    }
}
