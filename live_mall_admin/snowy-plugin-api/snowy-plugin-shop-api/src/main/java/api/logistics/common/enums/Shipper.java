package api.logistics.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>常用快递公司</p>
 *
 * @author goya
 * @since 2025/4/24 16:45
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Shipper {

    ZTO("ZTO", "中通快递"),
    STO("STO", "申通快递"),
    YTO("YTO", "圆通速递"),
    YD("YD", "韵达速递"),
    YZPY("YZPY", "邮政快递包裹"),
    EMS("EMS", "EMS"),
    SF("SF", "顺丰速运"),
    JD("JD", "京东快递"),
    JDKY("JDKY", "京东快运"),
    UC("UC", "优速快递"),
    DBL("DBL", "德邦快递"),
    JTSD("JTSD", "极兔速递"),
    CNSD("CNSD", "菜鸟速递"),
    CNCY("CNCY", "菜鸟橙运"),
    DNWL("DNWL", "丹鸟物流"),

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
    public static Shipper getByCode(String code) {
        for (Shipper type : Shipper.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public static Shipper getByName(String name) {
        for (Shipper type : Shipper.values()) {
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
