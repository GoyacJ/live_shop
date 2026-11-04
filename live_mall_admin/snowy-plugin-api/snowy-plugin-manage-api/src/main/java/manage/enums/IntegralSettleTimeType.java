package manage.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * <p>积分结算时间类型</p>
 *
 * @author goya
 * @since 2025/5/29 15:26
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IntegralSettleTimeType {

    IMMEDIATE(0, "立即结算"),

    FIXED_TIME(1, "固定时间结算"),

    ;

    @EnumValue
    private final Integer code;

    @JsonValue
    private final String name;

    /**
     * 根据 code 获取枚举实例
     *
     * @param code 枚举中的 code 字段值
     * @return 匹配的枚举实例，若无匹配则返回 null
     */
    @JsonCreator
    public static IntegralSettleTimeType getByCode(Integer code) {
        for (IntegralSettleTimeType type : IntegralSettleTimeType.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }

    public static IntegralSettleTimeType getByName(String name) {
        for (IntegralSettleTimeType type : IntegralSettleTimeType.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
