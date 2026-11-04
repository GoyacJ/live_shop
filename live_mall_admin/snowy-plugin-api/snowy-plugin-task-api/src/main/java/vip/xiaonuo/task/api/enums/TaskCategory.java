package vip.xiaonuo.task.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * <p>任务分类</p>
 *
 * @author goya
 * @since 2025/5/28 13:45
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaskCategory {

    SYSTEM("1", "系统"),
    SIGN_IN("2", "签到"),
    SHOP("3", "商城"),
    LIVE("4", "直播"),
    VIDEO("5", "短视频"),
    ADVERTISEMENT("6", "广告"),
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
    public static TaskCategory getByCode(String code) {
        for (TaskCategory type : TaskCategory.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }

    public static TaskCategory getByName(String name) {
        for (TaskCategory type : TaskCategory.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
