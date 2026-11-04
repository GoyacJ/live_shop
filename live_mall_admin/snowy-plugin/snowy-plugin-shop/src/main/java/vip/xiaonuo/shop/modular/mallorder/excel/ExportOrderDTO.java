package vip.xiaonuo.shop.modular.mallorder.excel;

import api.logistics.common.enums.Shipper;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import vip.xiaonuo.common.enums.OrderStatusEnum;
import vip.xiaonuo.shop.core.excel.converter.OrderStatusConverter;
import vip.xiaonuo.shop.core.excel.converter.ShipperConverter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/6 17:56
 */
@Data
public class ExportOrderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1186124512660303806L;

    /** 订单编号 */
    @ApiModelProperty(value = "订单号")
    @ExcelProperty("订单号")
    @ColumnWidth(30)
    private String orderNumber;

    /** 订单价格 */
    @ApiModelProperty(value = "订单实收金额")
    @ExcelProperty("订单实收金额(元)")
    @ColumnWidth(18)
    private BigDecimal orderFee;

    /** 成本价 */
    @ApiModelProperty(value = "成本价金额")
    @ExcelProperty("成本价金额(元)")
    @ColumnWidth(18)
    private BigDecimal costPrice;

    /** 价格 */
    @ApiModelProperty(value = "直播间价格")
    @ExcelProperty("直播间价格(元)")
    @ColumnWidth(18)
    private BigDecimal livePrice;

    /** 退款金额 */
    @ApiModelProperty(value = "退款金额")
    @ExcelProperty("退款金额(元)")
    @ColumnWidth(18)
    private BigDecimal refundAmount;

    /** 买家ID */
    @ApiModelProperty(value = "买家ID")
    @ExcelProperty("买家ID")
    @ColumnWidth(25)
    private String buyerId;

    /** 买家姓名 */
    @ApiModelProperty(value = "买家姓名")
    @ExcelProperty("买家")
    @ColumnWidth(25)
    private String buyerName;

    /** 商品名称 */
    @ApiModelProperty(value = "商品名称")
    @ExcelProperty("商品名称")
    @ColumnWidth(50)
    private String goodsName;

    /** 商品编码 */
    @ApiModelProperty(value = "商品编码")
    @ExcelProperty("商品编码")
    @ColumnWidth(25)
    private String goodsCode;

    /** 商品属性 */
    @ApiModelProperty(value = "商品属性")
    @ExcelProperty("商品属性")
    @ColumnWidth(30)
    private String goodsAttributes;

    /** 购买数量 */
    @ApiModelProperty(value = "购买数量")
    @ExcelProperty("购买数量")
    @ColumnWidth(10)
    private Integer purchaseQuantity;

    /** 商品售价 */
    @ApiModelProperty(value = "商品售价")
    @ExcelProperty("商品售价")
    @ColumnWidth(18)
    private BigDecimal goodsPrice;

    /** 成交金额（元） */
    @ApiModelProperty(value = "成交金额(元)")
    @ExcelProperty("成交金额（元）")
    @ColumnWidth(18)
    private BigDecimal transactionAmount;

    /** 交易状态 */
    @ApiModelProperty(value = "交易状态")
    @ExcelProperty("交易状态")
    @ColumnWidth(10)
    private String payStatus;

    /** 订单状态 */
    @ApiModelProperty(value = "订单状态")
    @ExcelProperty(value = "订单状态",converter = OrderStatusConverter.class)
    @ColumnWidth(10)
    private OrderStatusEnum orderStatus;

    /** 收货人 */
    @ApiModelProperty(value = "收货人")
    @ExcelProperty("收货人")
    @ColumnWidth(25)
    private String consignee;

    /** 联系电话 */
    @ApiModelProperty(value = "联系电话")
    @ExcelProperty("联系电话")
    @ColumnWidth(20)
    private String phoneNumber;

    /** 收货地址 */
    @ApiModelProperty(value = "收货地址")
    @ExcelProperty("收货地址")
    @ColumnWidth(50)
    private String deliveryAddress;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    @ExcelProperty("备注")
    @ColumnWidth(30)
    private String remark;

    /** 下单时间 */
    @ApiModelProperty(value = "下单时间")
    @ExcelProperty("下单时间")
    @ColumnWidth(30)
    private LocalDateTime orderPlacementTime;

    /** 付款时间 */
    @ApiModelProperty(value = "付款时间")
    @ExcelProperty("付款时间")
    @ColumnWidth(30)
    private LocalDateTime paymentTime;

    /** 发货时间 */
    @ApiModelProperty(value = "发货时间")
    @ExcelProperty("发货时间")
    @ColumnWidth(30)
    private LocalDateTime deliveryTime;

    /** 支付方式 */
    @ApiModelProperty(value = "支付方式")
    @ExcelProperty("支付方式")
    @ColumnWidth(10)
    private String paymentMethod;

    /** 直播间 */
    @ApiModelProperty(value = "直播间")
    @ExcelProperty("直播间")
    @ColumnWidth(30)
    private String liveName;

    /** 快递公司 */
    @ApiModelProperty(value = "快递公司")
    @ExcelProperty(value = "快递公司",converter = ShipperConverter.class)
    @ColumnWidth(20)
    private Shipper shipper;

    /** 快递单号 */
    @ApiModelProperty(value = "快递单号")
    @ExcelProperty("快递单号")
    @ColumnWidth(30)
    private String logisticsNumber;

    /** 买家留言 */
    @ApiModelProperty(value = "买家留言")
    @ExcelProperty("买家留言")
    @ColumnWidth(50)
    private String buyerMessage;

}
