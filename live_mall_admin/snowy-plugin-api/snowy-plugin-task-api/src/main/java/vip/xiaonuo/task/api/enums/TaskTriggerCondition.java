package vip.xiaonuo.task.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * <p>积分触发条件</p>
 *
 * @author goya
 * @since 2025/5/28 13:45
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaskTriggerCondition {

    AUTO("0", "自动触发"),
    INVITATION("1", "邀请"),
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
    public static TaskTriggerCondition getByCode(String code) {
        for (TaskTriggerCondition type : TaskTriggerCondition.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }

    public static TaskTriggerCondition getByName(String name) {
        for (TaskTriggerCondition type : TaskTriggerCondition.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
