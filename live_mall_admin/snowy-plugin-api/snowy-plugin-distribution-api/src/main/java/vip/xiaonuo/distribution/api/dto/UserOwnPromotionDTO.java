package vip.xiaonuo.distribution.api.dto;

import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/4 17:42
 */
public record UserOwnPromotionDTO(
        @ApiModelProperty("推广收益")
        BigDecimal promotionProfit,
        @ApiModelProperty("推广明细")
        Page<UserOwnPromotionDetailDTO> promotionDetails
) {

    public record UserOwnPromotionDetailDTO(
            @ApiModelProperty("订单ID")
            String orderId,
            @ApiModelProperty("订单编号")
            String orderNumber,
            @ApiModelProperty("推广名称")
            String promotionName,
            @ApiModelProperty("积分")
            BigDecimal integral,
            @ApiModelProperty("结算时间")
            LocalDateTime settleTime,
            @ApiModelProperty("结算状态")
            String settlementStatus
    ) {

    }
}
