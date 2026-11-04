package vip.xiaonuo.distribution.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>积分返佣方式</p>
 *
 * @author goya
 * @since 2025/5/21 11:11
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RecommendCommission {

    /**
     * 固定积分
     */
    FIXED("1", "固定积分"),

    /**
     * 按比例获取积分
     */
    RATIO("2", "按比例获取积分"),

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
    public static RecommendCommission getByCode(String code) {
        for (RecommendCommission type : RecommendCommission.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public static RecommendCommission getByName(String name) {
        for (RecommendCommission type : RecommendCommission.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
