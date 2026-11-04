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
package vip.xiaonuo.auth.modular.mobile.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.*;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.UserInfo;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import vip.xiaonuo.auth.api.SaBaseLoginUserApi;
import vip.xiaonuo.auth.core.enums.SaClientTypeEnum;
import vip.xiaonuo.auth.core.pojo.SaBaseClientLoginUser;
import vip.xiaonuo.auth.core.util.StpClientUtil;
import vip.xiaonuo.auth.modular.login.enums.AuthDeviceTypeEnum;
import vip.xiaonuo.auth.modular.login.enums.AuthExceptionEnum;
import vip.xiaonuo.auth.modular.login.param.AuthGetPhoneValidCodeParam;
import vip.xiaonuo.auth.modular.mobile.param.MobileAuthLoginParam;
import vip.xiaonuo.auth.modular.mobile.result.MobileUserInfoResult;
import vip.xiaonuo.auth.modular.mobile.service.MobileAuthService;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.consts.CacheConstant;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.util.CommonCryptogramUtil;
import vip.xiaonuo.common.util.CommonEmailUtil;
import vip.xiaonuo.common.util.CommonIpAddressUtil;
import vip.xiaonuo.common.util.CommonServletUtil;
import vip.xiaonuo.dev.api.DevSmsApi;
import vip.xiaonuo.mobile.api.auth.api.MobileAuthApi;
import vip.xiaonuo.mobile.api.auth.api.MobileUserInfoApi;
import vip.xiaonuo.mobile.api.auth.pojo.MobileLoginUser;
import vip.xiaonuo.mobile.api.auth.pojo.MobileUserInfo;

import java.util.List;
import java.util.stream.Collectors;

import static vip.xiaonuo.common.util.CommonServletUtil.getRequest;

/**
 * 移动端登录Service接口实现类
 *
 */
@Component
public class MobileAuthServiceImpl implements MobileAuthService {

    private static final String LOGIN_ERROR_TIMES_KEY_PREFIX = "login-error-times:";

    private static final String AUTH_VALID_CODE_CACHE_KEY = "auth-validCode:";

    @Inject("mobileAuthApi")
    private MobileAuthApi mobileAuthApi;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Inject("mobileUserInfoApi")
    private MobileUserInfoApi mobileUserInfoApi;

    @Inject
    private DevSmsApi devSmsApi;

    @Override
    public CommonResult MobileLogin(MobileAuthLoginParam mobileAuthLoginParam) {
        Context request = getRequest();
        // 获取手机号
        String mobile = mobileAuthLoginParam.getMobile();
        // 校验参数
        validPhoneValidCodeParam(mobile, mobileAuthLoginParam.getValidCode());
        //登录ip
        String ip = CommonIpAddressUtil.getIp(getRequest());
        //token
        String token = RandomUtil.randomString(10);
        //判断是登录还是注册
        CommonResult commonResult = mobileUserInfoApi.getUserInfoByMobile(mobile);
        if(commonResult.getData() == null){
            //注册
            JSONObject addParamJson = makeAddParam(mobile);
            mobileUserInfoApi.addUserInfo(addParamJson);
            MobileUserInfoResult mobileUserInfoResult = addParamJson.toBean(MobileUserInfoResult.class);
            mobileUserInfoResult.setToken(token);
            //将token作为登录令牌存入redis
            commonCacheOperator.put("Authorization", token+mobileUserInfoResult.getId()+ System.currentTimeMillis(),7*24*60*60);
            return CommonResult.data(mobileUserInfoResult);
        }
        JSONObject jsonObject = JSONUtil.parseObj(commonResult.getData());
        MobileUserInfoResult mobileUserInfoResult = jsonObject.toBean(MobileUserInfoResult.class);
        mobileUserInfoResult.setToken(token);
        //将token作为登录令牌存入redis
        commonCacheOperator.put("Authorization", token+mobileUserInfoResult.getId()+ System.currentTimeMillis(),7*24*60*60);
        return CommonResult.data(mobileUserInfoResult);
    }

    /**
     * 是否封禁状态
     * 如果被封禁了，执行以下逻辑，返回前端还需等待的时间
     */
    private void isDisableTime(String userAccount) {
        // disableTime = -2表示未被封禁
        long disableTime = StpUtil.getDisableTime(userAccount);
        if (disableTime > 0) {
            if (disableTime > 60) {
                throw new CommonException(userAccount + "账号已被封禁, 请再"+ disableTime/60+ "分钟后重新尝试登录!!");
            }
            throw new CommonException(userAccount + "账号已被封禁, 请再"+ disableTime+ "秒后重新尝试登录!!");
        }
    }

    @Override
    public CommonResult getPhoneValidCode(MobileAuthLoginParam mobileAuthLoginParam) {
        // 手机号
        String phone = mobileAuthLoginParam.getMobile();
        // 验证码
        String validCode = mobileAuthLoginParam.getValidCode();
     /*   // 校验参数
        validPhoneValidCodeParam(null, validCode);*/
        // 生成手机验证码的值，随机6为数字
        String phoneValidCode = RandomUtil.randomNumbers(6);

        // TODO 使用阿里云执行发送验证码，将验证码作为短信内容的参数变量放入，
        // TODO 签名不传则使用系统默认配置的签名，支持传入多个参数，示例：{"name":"张三","number":"15038****76"}
        //devSmsApi.sendSmsAliyun(phone, null, "验证码模板号", JSONUtil.toJsonStr(JSONUtil.createObj().set("validCode", phoneValidCode)));

        // TODO 使用腾讯云执行发送验证码，将验证码作为短信内容的参数变量放入，
        // TODO sdkAppId和签名不传则使用系统默认配置的sdkAppId和签名，支持传入多个参数，逗号拼接，示例："张三,15038****76,进行中"
        devSmsApi.sendSmsTencent("140052****", phone, "小诺开源技术", "1502***", phoneValidCode);

        // 将请求号作为key，验证码的值作为value放到redis，用于校验，5分钟有效
        commonCacheOperator.put(AUTH_VALID_CODE_CACHE_KEY + StrUtil.UNDERLINE + phone , phoneValidCode, 5 * 60);
        // 返回请求号
        return CommonResult.ok();
    }
    /**
     * 填充用户信息
     *
     */
    private JSONObject makeAddParam(String mobile) {
       JSONObject addParamJson = new JSONObject();
       addParamJson.put("mobile", mobile);
       addParamJson.put("lastLoginIp", CommonIpAddressUtil.getIp(getRequest()));
       addParamJson.put("lastLoginTime", System.currentTimeMillis());
       //替换手机号中间四个为昵称
        String beginStr = mobile.substring(0, 3);
        String endStr = mobile.substring(7);
       addParamJson.put("nickName", beginStr+"****"+endStr);
       addParamJson.put("levelId", "0");
       addParamJson.put("integral", 0);
       addParamJson.put("frozen_integral", 0);
       addParamJson.put("cash", 0);
       addParamJson.put("frozen_cash", 0);
       return addParamJson;
    }

    /**
     * 校验手机号与验证码等参数
     *
     **/
    private void validPhoneValidCodeParam(String mobile, String validCode) {
        // 验证码正确则校验手机号格式
        if(ObjectUtil.isEmpty(mobile)) {
            // 执行校验验证码
            validValidCode(null, validCode);
        } else {
            if(!PhoneUtil.isMobile(mobile)) {
                throw new CommonException(AuthExceptionEnum.PHONE_FORMAT_ERROR.getValue());
            }
            // 执行校验验证码
            validValidCode(mobile, validCode);
        }
    }
    /**
     * 校验验证码方法
     *
     * @author xuyuxiang
     * @date 2022/8/25 15:26
     **/
    private void validValidCode(String mobile, String validCode) {
        // 依据请求号，取出缓存中的验证码进行校验
        Object existValidCode;
        if(ObjectUtil.isEmpty(mobile)) {
            existValidCode = commonCacheOperator.get(AUTH_VALID_CODE_CACHE_KEY);
        } else {
            existValidCode = commonCacheOperator.get(AUTH_VALID_CODE_CACHE_KEY + StrUtil.UNDERLINE + mobile);
        }
        // 为空则直接验证码错误
        if(ObjectUtil.isEmpty(existValidCode)) {
            throw new CommonException(AuthExceptionEnum.VALID_CODE_ERROR.getValue());
        }
        // 移除该验证码
        if(ObjectUtil.isEmpty(mobile)) {
            commonCacheOperator.remove(AUTH_VALID_CODE_CACHE_KEY );
        } else {
            commonCacheOperator.remove(AUTH_VALID_CODE_CACHE_KEY + StrUtil.UNDERLINE + mobile);
        }
        // 不一致则直接验证码错误
        if (!validCode.equals(Convert.toStr(existValidCode).toLowerCase())) {
            throw new CommonException("验证码错误");
        }
    }
}
