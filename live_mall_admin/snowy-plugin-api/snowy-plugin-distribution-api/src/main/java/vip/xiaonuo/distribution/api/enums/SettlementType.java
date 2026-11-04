package vip.xiaonuo.distribution.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>积分结算方式</p>
 *
 * @author goya
 * @since 2025/5/21 10:36
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SettlementType {

    AUTO("1", "自动结算"),
    MANUAL("2", "人工结算"),
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
    public static SettlementType getByCode(String code) {
        for (SettlementType type : SettlementType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public static SettlementType getByName(String name) {
        for (SettlementType type : SettlementType.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
