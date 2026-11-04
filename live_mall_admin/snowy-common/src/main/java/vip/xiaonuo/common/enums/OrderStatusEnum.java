package vip.xiaonuo.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderStatusEnum {

    //订单状态  售后状态枚举
    PENDING_PAYMENT(0, "待支付"),
    PAID(1, "已支付待发货"),
    PROCESSING(2, "已发货待收货"),
    SHIPPED(3, "已收货"),
    COMPLETED(4, "已完成"),

    RETURN_GOODS(5, "售后"),
    CANCEL(6, "已取消");

    @EnumValue
    private final int status;

    @JsonValue
    private final String description;

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据给定的状态值返回对应的OrderStatusEnum枚举值。
     *
     * @param status 给定的状态值
     * @return 返回对应的OrderStatusEnum枚举值
     */
    @JsonCreator
    public static OrderStatusEnum fromStatus(int status) {
        for (OrderStatusEnum orderStatus : OrderStatusEnum.values()) {
            if (orderStatus.getStatus() == status) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("Invalid order status: " + status);
    }

    public static OrderStatusEnum getByName(String name) {
        for (OrderStatusEnum type : OrderStatusEnum.values()) {
            if (type.getDescription().contains(name)) {
                return type;
            }
        }
        return null;
    }
}
