package manage.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>限制用户类型</p>
 *
 * @author goya
 * @since 2025/5/22 11:09
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserLimitType {

    ALL("1", "所有用户"),

    SYSTEM("2", "系统用户"),

    NORMAL("3", "普通用户"),

    SPECIAL("4", "特殊用户"),

    ;

    @EnumValue
    @JsonValue
    private final String code;

    private final String name;

    /**
     * 根据 code 获取枚举实例
     *
     * @param code 枚举中的 code 字段值
     * @return 匹配的枚举实例，若无匹配则返回 null
     */
    @JsonCreator
    public static UserLimitType getByCode(String code) {
        for (UserLimitType type : UserLimitType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public static UserLimitType getByName(String name) {
        for (UserLimitType type : UserLimitType.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
