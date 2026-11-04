package vip.xiaonuo.shop.modular.logistics.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.pojo.CommonEntity;
import api.logistics.common.enums.KdniaoLogisticsState;
import api.logistics.common.enums.KdniaoLogisticsStateEx;
import api.logistics.common.enums.Shipper;

import java.io.Serial;

/**
 * <p>订单物流信息</p>
 *
 * @author goya
 * @since 2025/4/27 09:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("mall_order_logistics")
public class OrderLogistics extends CommonEntity {

    @Serial
    private static final long serialVersionUID = 3364369565213068776L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号", position = 2)
    private String orderCode;

    /**
     * 物流编号
     */
    @ApiModelProperty(value = "物流编号", position = 3)
    private String logisticCode;

    /**
     * 物流公司
     */
    @ApiModelProperty(value = "物流公司", position = 4)
    private Shipper shipper;

    /**
     * 寄件人or收件人 手机号后四位
     */
    @ApiModelProperty(value = "手机尾号后四位", position = 5)
    private String customerName;

    /**
     * 物流状态
     */
    @ApiModelProperty(value = "物流状态", position = 6)
    private KdniaoLogisticsState state;

    /*
      扩展物流状态
     */
    private KdniaoLogisticsStateEx stateEx;

    /**
     * 当前所在城市
     */
    @ApiModelProperty(value = "当前所在城市", position = 7)
    private String location;

    /**
     * 下一站城市
     */
    @ApiModelProperty(value = "下一城市", position = 8)
    private String nextCity;

    /**
     * 配送员
     */
    @ApiModelProperty(value = "配送员", position = 9)
    private String deliveryMan;

    /**
     * 配送员电话
     */
    @ApiModelProperty(value = "配送员电话\t", position = 10)
    private String deliveryManTel;
}
