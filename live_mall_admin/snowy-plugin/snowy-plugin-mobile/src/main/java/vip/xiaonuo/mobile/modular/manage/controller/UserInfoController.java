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
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.UploadedFile;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.manage.modular.userinfo.param.UserInfoEditParam;
import vip.xiaonuo.manage.modular.userinfo.param.UserNameAndMobileEditParam;
import vip.xiaonuo.mobile.modular.manage.param.PromotionParam;
import vip.xiaonuo.mobile.modular.manage.result.UserInfoResult;
import vip.xiaonuo.mobile.modular.manage.service.UserInfoService;
import vip.xiaonuo.mobile.modular.manage.vo.UserPromotionVO;

import java.io.IOException;

/**
 * 移动端用户列表控制器
 *
 */
@Api(tags = "用户列表控制器")
@SaIgnore
@Controller
@Valid
@Slf4j
public class UserInfoController {
    @Inject
    private UserInfoService userInfoService;

    /**
     * 根据手机号获取用户详情
     *
     */
    @ApiOperation("移动端根据手机号获取用户详情")
    @Get
    @Mapping("/mapi/auth/getUserDetail")
    public CommonResult<UserInfoResult> getUserinfo(Context context) {
        Object uid = context.attr("uid");
        if (ObjectUtil.isEmpty(uid)) {
            return CommonResult.error("用户未登录");
        }

        return CommonResult.data(userInfoService.getUserinfo(Convert.toStr(uid)));
    }

    @Post
    @Mapping("/test/upload")
    public CommonResult<String> upload(UploadedFile avatar) throws IOException {
        try{
            log.warn("avatar-upload:{}", avatar);
            System.out.println(avatar.getContentType());
            // avatar.transferTo(new File("/demo/user/logo.jpg")); //把它转为本地文件
        } catch (Exception e) {
            return CommonResult.error("上传失败"+e.getMessage());
        } finally {
            //用完之后，删除"可能的"临时文件 //v2.7.2 后支持
            avatar.delete();
        }

        return CommonResult.data(avatar.getName());
    }

    /**
     * 移动端修改用户头像
     *
     */
    @Post
    @Mapping("/mapi/auth/editUserAvatar")
    public CommonResult<String> editUserAvatar(Context context) {
        UploadedFile avatar;
        try {
            avatar = context.file("avatar");
            log.warn("avatar:{}", avatar);
        } catch (Exception e) {
            return CommonResult.error("请上传图片");
        }
        if (avatar == null) {
            return CommonResult.error("请上传图片");
        }
        return CommonResult.data(userInfoService.editUserAvatar(context.attrOrDefault("uid", "-"), avatar));
    }

    /**
     * 移动端修改用户头像
     *
     */
    @SaIgnore
    @ApiOperation("移动端修改用户昵称手机号")
    @Post
    @Mapping("/mapi/auth/updateUserinfo")
    public CommonResult updateUserinfo(@Validated UserNameAndMobileEditParam userNameAndMobileEditParam) {
        userInfoService.editUserName(userNameAndMobileEditParam);
        return CommonResult.ok();
    }


    /**
     * 编辑用户列表
     *
     * @author zhaishaojiang
     * @date  2025/02/17 17:28
     */
    @SaIgnore
    @ApiOperation("编辑用户列表")
    @Post
    @Mapping("/mobile/userinfo/edit")
    public CommonResult<String> edit(UserInfoEditParam userInfoEditParam) {
        userInfoService.edit(userInfoEditParam);
        return CommonResult.ok();
    }

    // 我的推广
    @ApiOperation("我的推广")
    @Get
    @Mapping("/mapi/auth/user/promotion")
    public CommonResult<UserPromotionVO> promotion(@Validated PromotionParam promotionParam) {
        return CommonResult.data(userInfoService.promotion(promotionParam));
    }
}
