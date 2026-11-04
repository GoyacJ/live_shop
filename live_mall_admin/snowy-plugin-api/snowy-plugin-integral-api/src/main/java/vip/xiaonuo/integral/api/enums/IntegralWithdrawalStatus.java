package vip.xiaonuo.integral.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * <p>积分提现状态</p>
 *
 * @author goya
 * @since 2025/5/28 13:45
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum IntegralWithdrawalStatus {

    WITHDRAWAL("1", "已提现"),

    NOT_WITHDRAWAL("2", "未提现"),
    WITHDRAWING("3", "正在提现"),
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
    public static IntegralWithdrawalStatus getByCode(String code) {
        for (IntegralWithdrawalStatus type : IntegralWithdrawalStatus.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }

    public static IntegralWithdrawalStatus getByName(String name) {
        for (IntegralWithdrawalStatus type : IntegralWithdrawalStatus.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
