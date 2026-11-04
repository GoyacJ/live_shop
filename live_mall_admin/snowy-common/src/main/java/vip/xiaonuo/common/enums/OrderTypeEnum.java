package vip.xiaonuo.common.enums;

public enum OrderTypeEnum {

    //  订单类型，1商城订单，2直播间订单，3积分兑换订单，4直播间福袋
    SHOP_ORDER("1", "商城订单"),
    LIVE_ORDER("2", "直播间订单"),
    INTEGRAL_ORDER("3", "积分兑换订单"),
    LIVE_FU_DI_ORDER("4", "直播间福袋");

    private String type;
    private String description;

    OrderTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static OrderTypeEnum getByType(String type) {
        for (OrderTypeEnum orderType : OrderTypeEnum.values()) {
            if (orderType.getType().equalsIgnoreCase(type)) {
                return orderType;
            }
        }
        throw new IllegalArgumentException("Invalid order type: " + type);
    }
}
