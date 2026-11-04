package api.logistics.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/24 16:56
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum KdniaoLogisticsState {

    NONE(0, "暂无轨迹信息"),
    COLLECTED(1, "已揽收"),
    ON_THE_WAY(2, "在途中"),
    SIGN_FOR_RECEIPT(3, "签收"),
    PROBLEM_ITEM(4, "问题件"),
    FORWARD(5, "转寄"),
    CUSTOMS_CLEARANCE(6, "清关"),
    ;

    @EnumValue
    private final Integer code;

    private final String description;

    /**
     * 根据 code 获取枚举实例
     *
     * @param code 枚举中的 code 字段值
     * @return 匹配的枚举实例，若无匹配则返回 null
     */
    public static KdniaoLogisticsState getByCode(Integer code) {
        for (KdniaoLogisticsState type : KdniaoLogisticsState.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
