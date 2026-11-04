package api.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;
import vip.xiaonuo.common.enums.OrderTypeMapperEnum;

import java.math.BigDecimal;

/**
 * <p>创建订单</p>
 *
 * @author goya
 * @since 2025/4/24 11:30
 */
@Data
public class CreateOrderDTO {
    @NotBlank(message = "创建用户不能为空")
    private String createUserId;

    @NotBlank(message = "邀请人不能为空")
    private String pid;

    @NotBlank(message = "订单来源不能为空")
    private String sourceId;

    @NotBlank(message = "备注不能为空")
    private String remark;

    @NotNull(message = "订单类型不能为空")
    private OrderTypeMapperEnum orderType;

    @NotNull(message = "订单价格不能为空")
    private BigDecimal orderFee;

    @NotBlank(message = "商品不能为空")
    private String goodsId;

    @NotNull(message = "商品数量不能为空")
    private Integer goodsNum;

    /**
     * 收货人手机号
     */
    @ApiModelProperty(value = "手机号", position = 5)
    private String mobile;

    /**
     * 收货人昵称
     */
    @ApiModelProperty(value = "收货人昵称", position = 6)
    private String nickname;

    /**
     * 省
     */
    @ApiModelProperty(value = "省", position = 7)
    private String provinceName;

    /**
     * 市
     */
    @ApiModelProperty(value = "市", position = 8)
    private String cityName;

    /**
     * 县区
     */
    @ApiModelProperty(value = "县区", position = 9)
    private String countiesName;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址", position = 10)
    private String address;
}
