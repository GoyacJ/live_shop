package manage.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p></p>
 * 变更类型,1=任务奖励，2=购物奖励3=直播福利,4=直播福袋,5=直播红包,6=积分兑换,7=现金提现,8=积分抵扣
 * @author goya
 * @since 2025/5/21 16:26
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserBalanceChangeType {

    /**
     * 任务奖励
     */
    TASK_REWARD("1", "任务奖励"),

    /**
     * 购物奖励
     */
    SHOPPING_REWARD("2", "购物奖励"),

    /**
     * 直播福利
     */
    LIVE_BENEFITS("3", "直播福利"),

    /**
     * 直播福袋
     */
    LIVE_LUCKY_BAG("4", "直播福袋"),

    /**
     * 直播红包
     */
    LIVE_RED_ENVELOPE("5", "直播红包"),

    /**
     * 积分兑换
     */
    POINTS_REDEMPTION("6", "积分兑换"),

    /**
     * 现金提现
     */
    CASH_WITHDRAWAL("7", "现金提现"),

    /**
     * 积分抵扣
     */
    POINTS_DEDUCTION("8", "积分抵扣"),

    /**
     * 首次注册
     */
    FIRST_REGISTER("9", "首次注册"),

    /**
     * 推广返佣
     */
    PROMOTION_REBATES("10", "推广返佣"),

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
    public static UserBalanceChangeType getByCode(String code) {
        for (UserBalanceChangeType type : UserBalanceChangeType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    public static UserBalanceChangeType getByName(String name) {
        for (UserBalanceChangeType type : UserBalanceChangeType.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }

}
