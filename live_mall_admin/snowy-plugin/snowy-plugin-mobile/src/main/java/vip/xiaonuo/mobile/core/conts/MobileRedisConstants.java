package vip.xiaonuo.mobile.core.conts;

/**
 * Author: shaojiang
 * CreateTime: 2025/3/20 09:16
 * Description: 移动端Redis静态变量
 */
public interface MobileRedisConstants {
    /// 请求随机数前缀
    String API_REQUEST_NONCE = "mobile:request:nonce:";
    /// 用户token前缀
    String USER_TOKEN_PREFIX = "mobile:user:token:";
    String USER_TOKEN_H5_PREFIX = "h5:user:token:";
    /// 短信验证码前缀
    String USER_LOGIN_SMS_PREFIX = "mobile:user:login:sms:";

}
