package vip.xiaonuo.mobile.modular.h5.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryWrapper;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.util.CommonIpAddressUtil;
import vip.xiaonuo.dev.api.DevSmsApi;
import vip.xiaonuo.manage.modular.userinfo.entity.UserInfo;
import vip.xiaonuo.mobile.core.conts.MobileRedisConstants;
import vip.xiaonuo.mobile.modular.auth.menus.AuthExceptionEnum;
import vip.xiaonuo.mobile.modular.h5.param.H5LoginParam;
import vip.xiaonuo.mobile.modular.h5.service.H5LoginService;
import vip.xiaonuo.mobile.modular.manage.param.UserInfoRegisterParam;
import vip.xiaonuo.mobile.modular.manage.result.UserInfoResult;
import vip.xiaonuo.mobile.modular.manage.service.UserInfoService;

import java.util.Date;

import static cn.hutool.core.util.RandomUtil.randomString;
import static vip.xiaonuo.common.util.CommonServletUtil.getRequest;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/12 16:03
 * Description:
 */
@Tran
@Component
public class H5LoginServiceImpl implements H5LoginService {
    @Inject("${mobile.user.token-expire-time}")
    private int tokenExpireTime;
    @Inject("${mobile.user.login-sms-length}")
    private int loginSmsLength;
    @Inject("${mobile.user.login-sms-expire-time}")
    private int loginSmsExpireTime;

    @Inject
    private DevSmsApi devSmsApi;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Inject
    private UserInfoService userInfoService;

    @Override
    public String getCode(String phone) {
        // 生成手机验证码的值，随机6为数字
        String phoneValidCode = RandomUtil.randomNumbers(loginSmsLength);

        // TODO 使用阿里云执行发送验证码，将验证码作为短信内容的参数变量放入，
        // TODO 签名不传则使用系统默认配置的签名，支持传入多个参数，示例：{"name":"张三","number":"15038****76"}
        //devSmsApi.sendSmsAliyun(phone, null, "验证码模板号", JSONUtil.toJsonStr(JSONUtil.createObj().set("validCode", phoneValidCode)));

        // TODO 使用腾讯云执行发送验证码，将验证码作为短信内容的参数变量放入，
        // TODO sdkAppId和签名不传则使用系统默认配置的sdkAppId和签名，支持传入多个参数，逗号拼接，示例："张三,15038****76,进行中"
        /* devSmsApi.sendSmsTencent("140052****", phone, "小诺开源技术", "1502***", phoneValidCode);*/
        devSmsApi.sendSmsTencent("1400976997", phone, "三河市锐新科技", "2404469", phoneValidCode);

        // 将请求号作为key，验证码的值作为value放到redis，用于校验，5分钟有效
        commonCacheOperator.put(MobileRedisConstants.USER_LOGIN_SMS_PREFIX + phone, phoneValidCode, loginSmsExpireTime);
        return phoneValidCode;
    }

    @Override
    public CommonResult login(H5LoginParam loginParam) {
        // 获取手机号
        String mobile = loginParam.getPhone();
        // 校验参数
        validPhoneValidCodeParam(mobile, loginParam.getCode());
        //登录ip
        //生成用户邀请码
        String rcode = loginParam.getRcode();
        //token
        String token = randomString(10);
        //根据电话查询用户信息
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(mobile)) {
            queryWrapper.eq(UserInfo::getMobile, mobile);
        }
        UserInfo userInfo = this.userInfoService.getOne(queryWrapper);
        //如果用户信息为空则需要注册
        if (ObjectUtil.isEmpty(userInfo)) {
            userInfo = userInfoService.register(new UserInfoRegisterParam(
                    mobile,
                    rcode
            ));
        }
        userInfo.setLastLoginTime(new Date());
        userInfo.setLastLoginIp(CommonIpAddressUtil.getIp(getRequest()));
        userInfo.setLastLoginDevice("H5");
        this.userInfoService.updateById(userInfo);
        JSONObject jsonObject = JSONUtil.parseObj(userInfo);
        UserInfoResult mobileUserInfoResult = jsonObject.toBean(UserInfoResult.class);
        //将token作为登录令牌存入redis，有效时常为7天
        String tokenStr = token + userInfo.getId();
        String md5Str = DigestUtil.md5Hex(tokenStr);
        //将md5Str返回给移动端
        mobileUserInfoResult.setToken(md5Str);
        commonCacheOperator.put(MobileRedisConstants.USER_TOKEN_H5_PREFIX + md5Str, userInfo.getId(), tokenExpireTime);

        return CommonResult.data(mobileUserInfoResult);
    }

    private void validPhoneValidCodeParam(String mobile, String validCode) {
        if (!PhoneUtil.isMobile(mobile)) {
            throw new CommonException(AuthExceptionEnum.PHONE_FORMAT_ERROR.getValue());
        }
        if (ObjectUtil.isEmpty(validCode) || validCode.length() != loginSmsLength) {
            throw new CommonException(AuthExceptionEnum.VALID_CODE_ERROR.getValue());
        }

        // 依据请求号，取出缓存中的验证码进行校验
        Object existValidCode = commonCacheOperator.get(MobileRedisConstants.USER_LOGIN_SMS_PREFIX + mobile);
        // 为空或不一致则直接验证码错误
        if (ObjectUtil.isEmpty(existValidCode) || !validCode.equals(Convert.toStr(existValidCode))) {
            throw new CommonException(AuthExceptionEnum.VALID_CODE_ERROR.getValue());
        }

        // 移除该验证码
        commonCacheOperator.remove(MobileRedisConstants.USER_LOGIN_SMS_PREFIX + mobile);
    }
}
