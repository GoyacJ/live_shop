package vip.xiaonuo.mobile.api.order.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResult {

    /** ID */
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    /** 订单编号 */
    @ApiModelProperty(value = "订单编号", position = 2)
    private String orderNumber;

    /** 订单价格 */
    @ApiModelProperty(value = "订单价格", position = 3)
    private Double orderFee;

    /** 物流编号 */
    @ApiModelProperty(value = "物流编号", position = 4)
    private String logisticsNumber;

    /** 物流状态 */
    @ApiModelProperty(value = "物流状态", position = 5)
    private String logisticsStatus;

    /** 手机号 */
    @ApiModelProperty(value = "手机号", position = 6)
    private String mobile;

    /** 收货人昵称 */
    @ApiModelProperty(value = "收货人昵称", position = 7)
    private String nickname;

    /** 省 */
    @ApiModelProperty(value = "省", position = 8)
    private String provinceName;

    /** 市 */
    @ApiModelProperty(value = "市", position = 9)
    private String cityName;

    /** 县区 */
    @ApiModelProperty(value = "县区", position = 10)
    private String countiesName;

    /** 详细地址 */
    @ApiModelProperty(value = "详细地址", position = 11)
    private String address;

    /** 订单状态 */
    @ApiModelProperty(value = "订单状态", position = 12)
    private String status;

    /** 订单创建时间*/
    @ApiModelProperty(value = "订单创建时间", position = 13)
    private String orderTime;

    /** 确认收货时间 */
    @ApiModelProperty(value = "确认收货时间", position = 14)
    private String orderConfirmTime;
}
