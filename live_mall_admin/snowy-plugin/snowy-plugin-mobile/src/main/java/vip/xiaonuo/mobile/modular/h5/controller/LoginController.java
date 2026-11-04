package vip.xiaonuo.mobile.modular.h5.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.PhoneUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noear.solon.annotation.*;
import org.noear.solon.validation.annotation.Valid;
import org.noear.solon.validation.annotation.Validated;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.mobile.modular.h5.param.H5LoginParam;
import vip.xiaonuo.mobile.modular.h5.service.H5LoginService;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/12 15:58
 * Description: H5用户登录/注册
 */
@Api(tags = "H5用户登录/注册")
@Controller
@Valid
@SaIgnore
public class LoginController {
    @Inject
    private H5LoginService h5LoginService;

    @ApiOperation("获取验证码")
    @Get
    @Mapping("/h5/getCode")
    public CommonResult<String> getCode(String phone) {
        if (!PhoneUtil.isMobile(phone)) {
            return CommonResult.error("手机号码格式错误");
        }
        h5LoginService.getCode(phone);
        return CommonResult.ok();
    }

    @ApiOperation("登录/注册")
    @Post
    @Mapping("/h5/login")
    public CommonResult<String> login(@Validated H5LoginParam loginParam) {
        if (!PhoneUtil.isMobile(loginParam.getPhone())) {
            return CommonResult.error("手机号码格式错误");
        }
        return h5LoginService.login(loginParam);
    }
}
