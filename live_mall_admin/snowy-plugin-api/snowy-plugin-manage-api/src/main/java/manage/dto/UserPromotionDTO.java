package manage.dto;

import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/26 10:33
 */
public record UserPromotionDTO(
        @ApiModelProperty("邀请人数")
        Integer inviteNum,
        @ApiModelProperty("推广收益")
        BigDecimal promotionProfit,
        @ApiModelProperty("推广明细")
        Page<UserPromotionDetailDTO> promotionDetails
) {

    public record UserPromotionDetailDTO(
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
            String settlementStatus) {
    }
}
