package vip.xiaonuo.common.enums;

import lombok.Getter;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum ErrorCodeEnum {
    SUCCESS(0, "✅ 没有错误"),
    INTERNAL_SERVER_ERROR(500, "🚨 服务器内部错误"),
    PARAM_ERROR(1001, "❌ 参数错误"),
    PERMISSION_DENIED(1002, "🚫 权限不足"),
    DB_DUPLICATE_KEY(1003, "💽 数据库主键重复"),
    DB_RECORD_NOT_FOUND(1004, "🚫 数据库记录未找到"),
    USER_NOT_FOUND(1101, "🚫 用户 ID 不存在"),
    USER_ALREADY_REGISTERED(1102, "🚫 用户已经注册"),
    GROUP_NOT_FOUND(1201, "🚫 群不存在"),
    GROUP_ALREADY_EXISTS(1202, "🚫 群已存在"),
    USER_NOT_IN_GROUP(1203, "🚫 用户不在群组中"),
    GROUP_DISMISSED(1204, "🚫 群组已解散"),
    UNSUPPORTED_GROUP_TYPE(1205, "🚫 不支持的群类型"),
    GROUP_APPLICATION_PROCESSED(1206, "🚫 群申请已被处理"),
    CANNOT_ADD_SELF(1301, "🚫 不能添加自己为好友"),
    BLOCKED_BY_USER(1302, "🚫 已被对方拉黑"),
    NOT_YOUR_FRIEND(1303, "🚫 对方不是自己的好友"),
    ALREADY_FRIENDS(1304, "🚫 已是好友关系"),
    MESSAGE_READ_DISABLED(1401, "🚫 消息已读功能被关闭"),
    USER_MUTED(1402, "🚫 你已被禁言"),
    GROUP_MUTED(1403, "🚫 群已被禁言"),
    MESSAGE_RECALLED(1404, "🚫 消息已被撤回"),
    AUTHORIZATION_EXPIRED(1405, "🚫 授权过期"),
    TOKEN_EXPIRED(1501, "🚫 token 已过期"),
    TOKEN_INVALID(1502, "🚫 token 无效"),
    TOKEN_FORMAT_ERROR(1503, "🚫 token 格式错误"),
    TOKEN_NOT_EFFECTIVE(1504, "🚫 token 还未生效"),
    TOKEN_UNKNOWN_ERROR(1505, "🚫 未知 token 错误"),
    TOKEN_KICKED(1506, "🚫 被踢出的 token"),
    TOKEN_NOT_FOUND(1507, "🚫 token 不存在"),
    CONNECTION_LIMIT_EXCEEDED(1601, "🚫 连接数超过限制"),
    HANDSHAKE_ERROR(1602, "🚫 连接握手参数错误"),
    FILE_UPLOAD_EXPIRED(1701, "🚫 文件上传过期");

    private final int code;
    private final String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private static final Map<Integer, ErrorCodeEnum> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(ErrorCodeEnum::getCode, e -> e));

    public static ErrorCodeEnum fromCode(int code) {
        return CODE_MAP.getOrDefault(code, INTERNAL_SERVER_ERROR);
    }
}

