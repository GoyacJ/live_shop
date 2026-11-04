package vip.xiaonuo.distribution.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>积分发放时间状态</p>
 *
 * @author goya
 * @since 2025/5/21 10:36
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CommissionTimeStatus {

    SETTLED("1", "已发放"),
    UNSETTLED("2", "未发放"),
    WAIT_SETTLED("3", "正在发放"),

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
    public static CommissionTimeStatus getByCode(String code) {
        for (CommissionTimeStatus type : CommissionTimeStatus.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public static CommissionTimeStatus getByName(String name) {
        for (CommissionTimeStatus type : CommissionTimeStatus.values()) {
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
