package vip.xiaonuo.distribution.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>积分发放时间类型</p>
 *
 * @author goya
 * @since 2025/5/21 10:42
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommissionTimeType {


    /**
     * 积分结算完成立即发放
     */
    AUTO("1", "积分结算完成立即发放"),

    /**
     * 每月固定日期发放
     */
    CUSTOM("2", "每月固定日期发放"),

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
    public static CommissionTimeType getByCode(String code) {
        for (CommissionTimeType type : CommissionTimeType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public static CommissionTimeType getByName(String name) {
        for (CommissionTimeType type : CommissionTimeType.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }

}
