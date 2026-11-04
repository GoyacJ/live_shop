package api.order.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>结算状态</p>
 *
 * @author goya
 * @since 2025/5/21 10:36
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SettlementStatus {

    SETTLED("1", "已结算"),
    UNSETTLED("2", "未结算"),
    WAIT_SETTLED("3", "正在结算"),

    ;

    @EnumValue
    private final String code;

    private final String name;

    /**
     * 根据 code 获取枚举实例
     *
     * @param code 枚举中的 code 字段值
     * @return 匹配的枚举实例，若无匹配则返回 null
     */
    @JsonCreator
    public static SettlementStatus getByCode(String code) {
        for (SettlementStatus type : SettlementStatus.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public static SettlementStatus getByName(String name) {
        for (SettlementStatus type : SettlementStatus.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
