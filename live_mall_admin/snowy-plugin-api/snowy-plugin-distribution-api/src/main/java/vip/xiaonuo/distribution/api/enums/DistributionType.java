package vip.xiaonuo.distribution.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>推广类型</p>
 *
 * @author goya
 * @since 2025/5/21 10:42
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DistributionType {

    ONCE_REGISTER("1", "首次注册"),

    SHOPPING("2", "推广购物返佣"),

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
    public static DistributionType getByCode(String code) {
        for (DistributionType type : DistributionType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public static DistributionType getByName(String name) {
        for (DistributionType type : DistributionType.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }

}
