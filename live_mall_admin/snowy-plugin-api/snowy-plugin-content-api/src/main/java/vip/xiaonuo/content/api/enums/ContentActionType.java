package vip.xiaonuo.content.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * <p>视频分类</p>
 *
 * @author goya
 * @since 2025/5/28 13:45
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ContentActionType {

    LIKE("1", "点赞"),
    COLLECT("2", "收藏"),
    SHARE("3", "分享"),
    COMMENT("4", "评论"),
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
    public static ContentActionType getByCode(String code) {
        for (ContentActionType type : ContentActionType.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }

    public static ContentActionType getByName(String name) {
        for (ContentActionType type : ContentActionType.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
