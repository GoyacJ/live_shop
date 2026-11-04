package vip.xiaonuo.integral.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * <p>积分操作来源</p>
 *
 * @author goya
 * @since 2025/5/28 14:01
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IntegralOperationSource {


    USER("1", "用户"),
    ADMIN("2", "管理员"),
    SYSTEM("3", "系统自动"),

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
    public static IntegralOperationSource getByCode(String code) {
        for (IntegralOperationSource type : IntegralOperationSource.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }

    public static IntegralOperationSource getByName(String name) {
        for (IntegralOperationSource type : IntegralOperationSource.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
