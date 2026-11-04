package vip.xiaonuo.integral.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * <p>积分变动类型</p>
 *
 * @author goya
 * @since 2025/5/28 13:45
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IntegralChangeType {

    INCREASE("1", "增加积分"),
    DECREASE("2", "减少积分"),
    ADJUST("3", "调整积分"),
    UNLOCK("4", "解锁冻结积分"),
    CHANGE_STATUS("5", "修改积分状态"),
    EXPIRE("6", "积分过期"),
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
    public static IntegralChangeType getByCode(String code) {
        for (IntegralChangeType type : IntegralChangeType.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }

    public static IntegralChangeType getByName(String name) {
        for (IntegralChangeType type : IntegralChangeType.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
