package vip.xiaonuo.distribution.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>积分结算时间类型</p>
 *
 * @author goya
 * @since 2025/5/21 10:40
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SettlementTimeType {

    /**
     * 交易完成后结算
     */
    AUTO("1", "交易完成后结算"),
    /**
     * 用户无法发起退款申请后结算
     */
    CUSTOM("2", "用户无法发起退款申请后结算"),

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
    public static SettlementTimeType getByCode(String code) {
        for (SettlementTimeType type : SettlementTimeType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public static SettlementTimeType getByName(String name) {
        for (SettlementTimeType type : SettlementTimeType.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }

}
