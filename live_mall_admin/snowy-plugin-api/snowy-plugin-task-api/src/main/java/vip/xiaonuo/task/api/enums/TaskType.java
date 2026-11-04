package vip.xiaonuo.task.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * <p>任务类型</p>
 *
 * @author goya
 * @since 2025/5/28 13:45
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaskType {

    DAILY("1", "日常任务"),
    PERMANENT("2", "永久任务"),
    TIME_LIMITED("3", "限时任务"),
    BASIC("4", "基础任务"),
    BROWSE("5", "浏览任务"),
    NOVICE("6", "新手任务"),

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
    public static TaskType getByCode(String code) {
        for (TaskType type : TaskType.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }

    public static TaskType getByName(String name) {
        for (TaskType type : TaskType.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
