package vip.xiaonuo.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * <p>状态</p>
 *
 * @author goya
 * @since 2025/5/28 13:50
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SimpleStatus {


    ENABLE(0, "启用"),
    DISABLE(1, "禁用"),

    ;

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String name;

    /**
     * 根据 code 获取枚举实例
     *
     * @param code 枚举中的 code 字段值
     * @return 匹配的枚举实例，若无匹配则返回 null
     */
    @JsonCreator
    public static SimpleStatus getByCode(Integer code) {
        for (SimpleStatus type : SimpleStatus.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }

    public static SimpleStatus getByName(String name) {
        for (SimpleStatus type : SimpleStatus.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
