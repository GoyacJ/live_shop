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
package vip.xiaonuo.manage.modular.userinfo.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.Valid;
import vip.xiaonuo.common.annotation.CommonLog;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.CommonValidList;
import vip.xiaonuo.manage.modular.userinfo.entity.UserInfo;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoAddParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoEditParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoIdParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoPageParam;
import vip.xiaonuo.manage.modular.userinfo.result.UserInfoResult;
import vip.xiaonuo.manage.modular.userinfo.service.UserInfoService;

/**
 * 用户列表控制器
 *
 * @author zhaishaojiang
 * @date  2025/02/17 17:28
 */
@Api(tags = "用户列表控制器")
@Controller
@Valid
public class UserInfoController {

    @Inject
    private UserInfoService userInfoService;

    /**
     * 获取用户列表分页
     *
     * @author zhaishaojiang
     * @date  2025/02/17 17:28
     */
    @ApiOperation("获取用户列表分页")
    @SaCheckPermission("/manage/userinfo/page")
    @Get
    @Mapping("/manage/userinfo/page")
    public CommonResult<Page<UserInfo>> page(UserInfoPageParam userInfoPageParam) {
        return CommonResult.data(userInfoService.page(userInfoPageParam));
    }

    /**
     * 添加用户列表
     *
     * @author zhaishaojiang
     * @date  2025/02/17 17:28
     */
    @ApiOperation("添加用户列表")
    @CommonLog("添加用户列表")
    @SaCheckPermission("/manage/userinfo/add")
    @Post
    @Mapping("/manage/userinfo/add")
    public CommonResult<String> add(UserInfoAddParam userInfoAddParam) {
        userInfoService.add(userInfoAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑用户列表
     *
     * @author zhaishaojiang
     * @date  2025/02/17 17:28
     */
    @ApiOperation("编辑用户列表")
    @CommonLog("编辑用户列表")
    @SaCheckPermission("/manage/userinfo/edit")
    @Post
    @Mapping("/manage/userinfo/edit")
    public CommonResult<String> edit(UserInfoEditParam userInfoEditParam) {
        userInfoService.edit(userInfoEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除用户列表
     *
     * @author zhaishaojiang
     * @date  2025/02/17 17:28
     */
    @ApiOperation("删除用户列表")
    @CommonLog("删除用户列表")
    @SaCheckPermission("/manage/userinfo/delete")
    @Post
    @Mapping("/manage/userinfo/delete")
    public CommonResult<String> delete(@NotEmpty(message = "集合不能为空")
                                                   CommonValidList<UserInfoIdParam> userInfoIdParamList) {
        userInfoService.delete(userInfoIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取用户列表详情
     *
     * @author zhaishaojiang
     * @date  2025/02/17 17:28
     */
    @ApiOperation("获取用户列表详情")
    @SaCheckPermission("/manage/userinfo/detail")
    @Get
    @Mapping("/manage/userinfo/detail")
    public CommonResult<UserInfo> detail(UserInfoIdParam userInfoIdParam) {
        return CommonResult.data(userInfoService.detail(userInfoIdParam));
    }

    /**
     * 根据手机号获取用户详情
     *
     */
    @ApiOperation("移动端根据手机号获取用户详情")
    @SaCheckPermission("/manage/userinfo/detailbymobile")
    @Get
    @Mapping("/manage/userinfo/detailbymobile")
    public CommonResult<UserInfoResult> detail(String mobile) {
        return CommonResult.data(userInfoService.getUserInfoByMobile(mobile));
    }

    /**
     * 移动端修改用户头像
     *
     */
    @ApiOperation("移动端修改用户头像")
    @SaCheckPermission("/manage/userinfo/editUserAvatar")
    @Post
    @Mapping("/manage/userinfo/editUserAvatar")
    public CommonResult<String> detail(String mobile, UploadedFile file) {
        return CommonResult.data(userInfoService.editUserAvatar(mobile, file));
    }

    /**
     * 移动端修改用户头像
     *
     */
    @ApiOperation("移动端修改用户积分")
    @SaCheckPermission("/manage/userinfo/editUserIntegral")
    @Post
    @Mapping("/manage/userinfo/editUserIntegral")
    public CommonResult editUserIntegral(UserInfoEditParam userInfoEditParam) {
        userInfoService.editUserIntegral(userInfoEditParam);
        return CommonResult.ok();
    }
}
