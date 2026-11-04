package vip.xiaonuo.integral.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * <p>积分分类</p>
 *
 * @author goya
 * @since 2025/5/28 13:45
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IntegralCategory {

    INTEGRAL("1", "积分"),
    FROZEN_INTEGRAL("2", "冻结积分"),
    EXPIRE_INTEGRAL("3", "过期积分"),
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
    public static IntegralCategory getByCode(String code) {
        for (IntegralCategory type : IntegralCategory.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }

    public static IntegralCategory getByName(String name) {
        for (IntegralCategory type : IntegralCategory.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
