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
package vip.xiaonuo.auth.modular.mobile.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.auth.core.enums.SaClientTypeEnum;
import vip.xiaonuo.auth.core.pojo.SaBaseLoginUser;
import vip.xiaonuo.auth.modular.login.param.AuthAccountPasswordLoginParam;
import vip.xiaonuo.auth.modular.login.param.AuthGetPhoneValidCodeParam;
import vip.xiaonuo.auth.modular.login.param.AuthPhoneValidCodeLoginParam;
import vip.xiaonuo.auth.modular.login.result.AuthPicValidCodeResult;
import vip.xiaonuo.auth.modular.login.service.AuthService;
import vip.xiaonuo.auth.modular.mobile.param.MobileAuthLoginParam;
import vip.xiaonuo.auth.modular.mobile.service.MobileAuthService;
import vip.xiaonuo.common.pojo.CommonResult;

/**
 */
@Api(tags = "登录控制器")
@Controller
@Valid
public class MobileController {
//添加忽略校验
    @Inject
    private MobileAuthService mobileAuthService;

    /**
     * B端获取图片验证码
     *
     * @author xuyuxiang
     * @date 2022/7/8 9:26
     **/
    @ApiOperation("B端获取图片验证码")
    @Get
    @Mapping("/auth/mobile/getPhoneValidCode")
    public CommonResult<String> getPicCaptcha(MobileAuthLoginParam mobileAuthLoginParam) {
        return mobileAuthService.getPhoneValidCode(mobileAuthLoginParam);
    }


    /**
     * 登录
     *
     **/
    @ApiOperation("移动端登陆")
    @Post
    @Mapping("/auth/mobile/login")
    public CommonResult<String> login(@Validated MobileAuthLoginParam mobileAuthLoginParam) {
        return mobileAuthService.MobileLogin(mobileAuthLoginParam);
    }

}
