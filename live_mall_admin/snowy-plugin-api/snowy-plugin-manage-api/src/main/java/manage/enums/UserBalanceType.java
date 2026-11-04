package manage.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>余额类型，1=现金,2=冻结现金,3=积分,4=冻结积分</p>
 *
 * @author goya
 * @since 2025/5/21 16:27
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserBalanceType {


    /**
     * 现金
     */
    CASH("1", "现金"),

    /**
     * 冻结现金
     */
    FREEZE_CASH("2", "冻结现金"),

    /**
     * 积分
     */
    INTEGRAL("3", "积分"),

    /**
     * 冻结积分
     */
    FREEZE_INTEGRAL("4", "冻结积分"),

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
    public static UserBalanceType getByCode(String code) {
        for (UserBalanceType type : UserBalanceType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public static UserBalanceType getByName(String name) {
        for (UserBalanceType type : UserBalanceType.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }

}
