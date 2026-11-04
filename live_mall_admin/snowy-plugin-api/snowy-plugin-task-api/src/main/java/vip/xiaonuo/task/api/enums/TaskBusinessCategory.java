package vip.xiaonuo.task.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

import static vip.xiaonuo.task.api.enums.TaskCategory.*;

/**
 * <p>任务业务分类</p>
 *
 * @author goya
 * @since 2025/5/28 13:45
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaskBusinessCategory {

    //---------- 认证 ----------
    AUTH_REGISTER("101", "注册", SYSTEM),
    ADD_CUSTOMER_WECHAT("102", "添加客服微信", SYSTEM),

    //---------- 签到 ----------
    SIGN_IN_DAY("201", "每日签到", SIGN_IN),
    SIGN_IN_CONTINUOUS("202", "连续签到", SIGN_IN),

    //---------- 商城 ----------
    SHOP_VISIT("301", "商品浏览", SHOP),
    SHOP_BUY("302", "商品购买", SHOP),
    SHOP_SHARE("303", "商品分享", SHOP),
    SHOP_COMMENT("304", "商品评论", SHOP),

    //---------- 直播 ----------
    LIVE_VISIT("401", "直播观看", LIVE),
    LIVE_SHARE("402", "直播分享", LIVE),
    LIVE_SUBSCRIBE("403", "直播订阅", LIVE),
    LIVE_COMMENT("404", "直播评论", LIVE),
    LIVE_REWARD("405", "直播打赏", LIVE),
    LIVE_BUY("406", "直播商品购买", LIVE),
    LIVE_ACTIVITY("407", "直播活动", LIVE),
    LIVE_GIFT("408", "直播送礼物", LIVE),


    //---------- 短视频 ----------
    VIDEO_VISIT("501", "视频观看", VIDEO),
    VIDEO_SHARE("502", "视频分享", VIDEO),
    VIDEO_SUBSCRIBE("503", "视频订阅", VIDEO),
    VIDEO_COMMENT("504", "视频评论", VIDEO),
    VIDEO_ADVERTISEMENT("505", "视频广告", VIDEO),
    VIDEO_SHORT_PLAY("551", "短剧分享", VIDEO),

    //---------- 广告 ----------
    ADVERTISEMENT_VISIT("601", "看广告", ADVERTISEMENT),

    ;

    @EnumValue
    @JsonValue
    private final String code;

    private final String name;

    private final TaskCategory taskCategory;

    /**
     * 根据 code 获取枚举实例
     *
     * @param code 枚举中的 code 字段值
     * @return 匹配的枚举实例，若无匹配则返回 null
     */
    @JsonCreator
    public static TaskBusinessCategory getByCode(String code) {
        for (TaskBusinessCategory type : TaskBusinessCategory.values()) {
            if (Objects.equals(type.getCode(), code)) {
                return type;
            }
        }
        return null;
    }

    public static TaskBusinessCategory getByName(String name) {
        for (TaskBusinessCategory type : TaskBusinessCategory.values()) {
            if (type.getName().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
