package vip.xiaonuo.mobile.constants;

public class RedisKeyConstants {
    // 用户相关的键
    public static final String USER_KEY_PREFIX = "Authorization:";

    // 生成用户信息的完整键
    public static String getUserInfoKey(String userId) {
        return USER_KEY_PREFIX + userId;
    }

}
