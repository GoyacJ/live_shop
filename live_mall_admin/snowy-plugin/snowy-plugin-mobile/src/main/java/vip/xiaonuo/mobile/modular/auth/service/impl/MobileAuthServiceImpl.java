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
package vip.xiaonuo.mobile.modular.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.solon.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import manage.MobileDeviceInfoApi;
import manage.UserApi;
import manage.dto.MobileDeviceAccessDTO;
import org.apache.commons.lang3.StringUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.pojo.dto.CommonIdParam;
import vip.xiaonuo.common.util.CommonIpAddressUtil;
import vip.xiaonuo.dev.api.DevConfigApi;
import vip.xiaonuo.dev.api.DevSmsApi;
import vip.xiaonuo.live.api.openim.OpenImApi;
import vip.xiaonuo.manage.modular.userinfo.entity.UserInfo;
import vip.xiaonuo.manage.modular.userinfo.mapper.UserInfoMapper;
import vip.xiaonuo.mobile.api.auth.api.MobileAuthApi;
import vip.xiaonuo.mobile.core.conts.MobileRedisConstants;
import vip.xiaonuo.mobile.modular.auth.menus.AuthExceptionEnum;
import vip.xiaonuo.mobile.modular.auth.param.MobileAuthLoginParam;
import vip.xiaonuo.mobile.modular.auth.param.MobileAuthPicCaptchaParam;
import vip.xiaonuo.mobile.modular.auth.param.MobileDeviceAccessParam;
import vip.xiaonuo.mobile.modular.auth.result.ChatContactResult;
import vip.xiaonuo.mobile.modular.auth.service.MobileAuthService;
import vip.xiaonuo.mobile.modular.integral.service.IntegralService;
import vip.xiaonuo.mobile.modular.manage.param.UserInfoRegisterParam;
import vip.xiaonuo.mobile.modular.manage.result.UserInfoResult;
import vip.xiaonuo.mobile.modular.manage.service.UserInfoService;
import vip.xiaonuo.task.api.TaskApi;
import vip.xiaonuo.task.api.enums.TaskBusinessCategory;

import java.util.Date;
import java.util.Map;

import static cn.hutool.core.util.RandomUtil.randomString;
import static vip.xiaonuo.common.util.CommonServletUtil.getRequest;

/**
 * 移动端登录Service接口实现类
 */
@Slf4j
@Component
public class MobileAuthServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements MobileAuthService {
    @Inject("${mobile.user.token-expire-time}")
    private int tokenExpireTime;
    @Inject("${mobile.user.login-sms-length}")
    private int loginSmsLength;
    @Inject("${mobile.user.login-sms-expire-time}")
    private int loginSmsExpireTime;

    @Inject("mobileAuthApi")
    private MobileAuthApi mobileAuthApi;

    @Inject
    private CommonCacheOperator commonCacheOperator;

    @Inject
    private DevSmsApi devSmsApi;

    @Inject
    private UserInfoService userInfoService;

    @Inject
    private OpenImApi openImApi;
    @Inject
    private DevConfigApi devConfigApi;

    @Inject
    private MobileDeviceInfoApi mobileDeviceInfoApi;
    @Inject
    private UserApi userApi;
    @Inject
    private IntegralService integralService;

    @Inject
    private TaskApi taskApi;

    @Override
    @Tran
    public CommonResult mobileLogin(MobileAuthLoginParam mobileAuthLoginParam) {
        // 获取手机号
        String mobile = mobileAuthLoginParam.getMobile();
        // 校验参数
        validPhoneValidCodeParam(mobile, mobileAuthLoginParam.getValidCode());
        //登录ip
        //生成用户邀请码
        String rcode = mobileAuthLoginParam.getRcode();
        //token
        String token = randomString(10);
        //根据电话查询用户信息
        QueryWrapper queryWrapper = new QueryWrapper();
        if (ObjectUtil.isNotEmpty(mobile)) {
            queryWrapper.eq(UserInfo::getMobile, mobile);
        }
        UserInfo userInfo = this.getOne(queryWrapper);
        //如果用户信息为空则需要注册
        if (ObjectUtil.isEmpty(userInfo)) {
            userInfo = userInfoService.register(new UserInfoRegisterParam(
                    mobile,
                    rcode
            ));
        }
        JSONObject jsonObject = JSONUtil.parseObj(userInfo);
        UserInfoResult mobileUserInfoResult = jsonObject.toBean(UserInfoResult.class);
        //将token作为登录令牌存入redis，有效时常为7天
        String tokenStr = token + userInfo.getId();
        String md5Str = DigestUtil.md5Hex(tokenStr);
        //将md5Str返回给移动端
        mobileUserInfoResult.setToken(md5Str);
        commonCacheOperator.put(MobileRedisConstants.USER_TOKEN_PREFIX + md5Str, userInfo.getId(), tokenExpireTime);

        updateMobileDeviceInfo(userInfo.getId(), mobileAuthLoginParam.getClientId());

        userInfo.setLastLoginTime(new Date());
        userInfo.setLastLoginIp(CommonIpAddressUtil.getIp(getRequest()));
        userInfo.setLastLoginDevice("App");
        this.userInfoService.updateById(userInfo);

        return CommonResult.data(mobileUserInfoResult);
    }

    private boolean innerAccount(String mobile) {
        if (StringUtils.equals(mobile, "13800000000")) {
            return true;
        }
        return false;
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
                throw new CommonException(userAccount + "账号已被封禁, 请再" + disableTime / 60 + "分钟后重新尝试登录!!");
            }
            throw new CommonException(userAccount + "账号已被封禁, 请再" + disableTime + "秒后重新尝试登录!!");
        }
    }

    @Override
    public CommonResult<Void> getPhoneValidCode(MobileAuthPicCaptchaParam mobileAuthPicCaptchaParam) {
        // 手机号
        String phone = mobileAuthPicCaptchaParam.getMobile();

        // 生成手机验证码的值，随机6为数字
        String phoneValidCode = RandomUtil.randomNumbers(loginSmsLength);

        // TODO 使用阿里云执行发送验证码，将验证码作为短信内容的参数变量放入，
        // TODO 签名不传则使用系统默认配置的签名，支持传入多个参数，示例：{"name":"张三","number":"15038****76"}
        //devSmsApi.sendSmsAliyun(phone, null, "验证码模板号", JSONUtil.toJsonStr(JSONUtil.createObj().set("validCode", phoneValidCode)));

        // TODO 使用腾讯云执行发送验证码，将验证码作为短信内容的参数变量放入，
        // TODO sdkAppId和签名不传则使用系统默认配置的sdkAppId和签名，支持传入多个参数，逗号拼接，示例："张三,15038****76,进行中"


        devSmsApi.sendSmsTencent("1400976997", phone, "三河市锐新科技", "2404469", phoneValidCode);

        // 将请求号作为key，验证码的值作为value放到redis，用于校验，5分钟有效
//        commonCacheOperator.put(MobileRedisConstants.USER_LOGIN_SMS_PREFIX + phone, "1234", loginSmsExpireTime);
        commonCacheOperator.put(MobileRedisConstants.USER_LOGIN_SMS_PREFIX + phone, phoneValidCode, loginSmsExpireTime);
        // 返回请求号
        return CommonResult.ok();
    }


    @Override
    public String getPhoneValidCodeTest() {
        // 生成手机验证码的值，随机6为数字
        String phoneValidCode = RandomUtil.randomNumbers(loginSmsLength);
        devSmsApi.sendSmsTencent("1400976997", "13800000000", "三河市锐新科技", "2404469", phoneValidCode);
        return phoneValidCode;
    }

    /**
     * 校验手机号与验证码等参数
     **/
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

    @Override
    public ChatContactResult getChatContact() {
        ChatContactResult chatContactResult = new ChatContactResult();
        chatContactResult.setQrcode(devConfigApi.getValueByKey("CUSTOMER_QR_CODE"));
        chatContactResult.setMobile(devConfigApi.getValueByKey("CESTOMER_MOBILE"));
        return chatContactResult;
    }

    @Override
    public boolean createGroup(String groupId, String title) {
        Map<String, String> result = openImApi.createGroup(groupId, title);
        return true;
    }

    @Override
    public boolean deviceAccess(MobileDeviceAccessParam param) {
        // 更新用户登录设备
        MobileDeviceAccessDTO dto = new MobileDeviceAccessDTO(
                param.clientId(),
                param.brand(),
                param.model(),
                param.version(),
                param.simulator()
        );
        return mobileDeviceInfoApi.saveDeviceInfo(dto);
    }

    @Override
    public void addCustomerWechat(CommonIdParam userId) {
        taskApi.task(userId.id(), TaskBusinessCategory.ADD_CUSTOMER_WECHAT);
    }

    /**
     * 更新用户登录设备
     *
     * @param userId
     * @param clientId
     */
    private void updateMobileDeviceInfo(String userId, String clientId) {
        mobileDeviceInfoApi.updateMobileDeviceInfo(userId, clientId);
    }
}
