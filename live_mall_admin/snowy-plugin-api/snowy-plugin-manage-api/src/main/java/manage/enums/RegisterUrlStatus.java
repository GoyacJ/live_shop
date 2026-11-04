package manage.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/22 11:09
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RegisterUrlStatus {

    /**
     * 启用
     */
    ENABLE("0", "启用"),

    /**
     * 停用
     */
    DISABLE("1", "停用"),

    ;

    @EnumValue
    private final String code;

    @JsonValue
    private final String name;

    /**
     * 根据 code 获取枚举实例
     *
     * @param code 枚举中的 code 字段值
     * @return 匹配的枚举实例，若无匹配则返回 null
     */
    @JsonCreator
    public static RegisterUrlStatus getByCode(String code) {
        for (RegisterUrlStatus type : RegisterUrlStatus.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public static RegisterUrlStatus getByName(String name) {
        for (RegisterUrlStatus type : RegisterUrlStatus.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
