package vip.xiaonuo.mobile.modular.manage.vo;

import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.ApiModelProperty;
import manage.dto.UserPromotionDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/26 10:33
 */
public record UserPromotionVO(
        @ApiModelProperty("邀请人数")
        Integer inviteNum,
        @ApiModelProperty("推广收益")
        BigDecimal promotionProfit,
        @ApiModelProperty("推广明细")
        Page<UserPromotionDetailVO> promotionDetails
) {

    public record UserPromotionDetailVO(
            @ApiModelProperty("订单ID")
            String orderId,
            @ApiModelProperty("订单编号")
            String orderNumber,
            @ApiModelProperty("推广名称名称")
            String promotionName,
            @ApiModelProperty("积分")
            BigDecimal integral,
            @ApiModelProperty("结算时间")
            LocalDateTime settleTime,
            @ApiModelProperty("结算状态")
            String settlementStatus) {

        public static UserPromotionDetailVO of(UserPromotionDTO.UserPromotionDetailDTO dto) {
            return new UserPromotionDetailVO(dto.orderId(), dto.orderNumber(), dto.promotionName(), dto.integral(), dto.settleTime(), dto.settlementStatus());
        }

        public static Page<UserPromotionDetailVO> of(Page<UserPromotionDTO.UserPromotionDetailDTO> dto) {
            return dto.map(UserPromotionDetailVO::of);
        }

    }

    public static UserPromotionVO of(UserPromotionDTO dto) {
        return new UserPromotionVO(dto.inviteNum(), dto.promotionProfit(), UserPromotionDetailVO.of(dto.promotionDetails()));
    }
}
