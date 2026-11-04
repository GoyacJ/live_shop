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
package vip.xiaonuo.mobile.modular.auth.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.dev.api.DevAppVersionApi;
import vip.xiaonuo.dev.dto.LatestAppVersionDTO;
import vip.xiaonuo.mobile.modular.auth.param.MobileAuthLoginParam;
import vip.xiaonuo.mobile.modular.auth.param.MobileAuthPicCaptchaParam;
import vip.xiaonuo.mobile.modular.auth.param.MobileDeviceAccessParam;
import vip.xiaonuo.mobile.modular.auth.result.ChatContactResult;
import vip.xiaonuo.mobile.modular.auth.service.MobileAuthService;
import vip.xiaonuo.mobile.modular.manage.result.UserInfoResult;

/**
 *
 */
@SaIgnore
@Api(tags = "移动端登录控制器")
@Controller
@Valid
public class MobileController {
    //添加忽略校验
    @Inject
    private MobileAuthService mobileAuthService;

    @Inject
    private DevAppVersionApi devAppVersionApi;

    // @ApiOperation("获取APP版本号")
    // @Get
    // @Mapping("/getAppVersion")
    // public CommonResult<Map<String, String>> oldAppVersion() {
    //     Map<String, String> map = new HashMap<>();
    //     map.put("version", "1.0.5");
    //     map.put("download", "https://file.live.weilaizhihuigu.cn/live/2025/1.0.5.apk");
    //     return CommonResult.data(map);
    // }

    @ApiOperation("获取APP版本号")
    @Get
    @Mapping("/getAppVersion")
    public CommonResult<LatestAppVersionDTO> getAppVersion() {
        return CommonResult.data(devAppVersionApi.getLatestAppVersion());
    }


    @ApiOperation("移动端获取验证码")
    @Post
    @Mapping("/mapi/getPhoneValidCode")
    public CommonResult<Void> getPicCaptcha(MobileAuthPicCaptchaParam mobileAuthPicCaptchaParam) {
        return mobileAuthService.getPhoneValidCode(mobileAuthPicCaptchaParam);
    }

    @Post
    @Mapping("/sms/test")
    public CommonResult<String> test() {
        return CommonResult.ok(mobileAuthService.getPhoneValidCodeTest());
    }

    @ApiOperation("设备访问信息")
    @Post
    @Mapping("/device/access")
    public CommonResult<Void> deviceAccess(MobileDeviceAccessParam param) {
        if (mobileAuthService.deviceAccess(param)){
            return CommonResult.ok();
        }
        return CommonResult.error();
    }

    @ApiOperation("登录/注册")
    @Post
    @Mapping("/mapi/doLoginBySms")
    public CommonResult<UserInfoResult> login(@Validated MobileAuthLoginParam mobileAuthLoginParam) {
        return mobileAuthService.mobileLogin(mobileAuthLoginParam);
    }

    @ApiOperation("添加客服微信")
    @Post
    @Mapping("/mapi/add-customer-wechat")
    public CommonResult<Void> addCustomerWechat(@Validated CommonIdParam userId) {
         mobileAuthService.addCustomerWechat(userId);
         return CommonResult.ok();
    }

    @ApiOperation("移动端退出登录")
    @Get
    @Mapping("/auth/mobile/logout")
    public CommonResult<String> logout() {
        StpUtil.logout();
        return CommonResult.ok();
    }


    @Get
    @Mapping("/mapi/chat/contact")
    public CommonResult<ChatContactResult> getContact() {
        return CommonResult.data(mobileAuthService.getChatContact());
    }

    @Post
    @Mapping("/test/create/group")
    public CommonResult<String> createGroup(String id, String title) {
        return CommonResult.data(mobileAuthService.createGroup(id, title) ? "创建成功" : "创建失败");
    }
}
