package vip.xiaonuo.common.enums;

/**
 * @Date: 2025/4/1 17:06
 * @Author: lx
 * @Description: 订单状态枚举
 * @Version: 1.0
 **/
public enum MallOrderStatusEnum {

    // todo 有一个类似的枚举 OrderStatusEnum
    // 0待支付，1已支付待发货，2已发货待收货，3已收货，4已完成，5售后'
    WAIT_PAYMENT("0", "待支付"),
    PAID_PENDING_SHIPMENT("1", "已支付待发货"),
    SHIPPED_PENDING_RECEIPT("2", "已发货待收货"),
    RECEIVED("3", "已收货"),
    COMPLETED("4", "已完成"),
    AFTER_SALES("5", "售后");


    private String code;
    private String name;
    MallOrderStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

    public static MallOrderStatusEnum getByCode(String code) {
        for (MallOrderStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid order status code: " + code);
    }

}
