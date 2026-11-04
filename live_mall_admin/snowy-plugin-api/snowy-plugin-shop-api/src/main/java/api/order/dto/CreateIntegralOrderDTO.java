package api.order.dto;

import io.swagger.annotations.ApiModelProperty;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;

/**
 * <p>创建商城订单参数</p>
 *
 * @author goya
 * @since 2025/4/24 11:19
 */
public record CreateIntegralOrderDTO(
         /*
          创用户Id
         */
        @NotBlank(message = "创建用户不能为空")
        String createUserId,
        /*
         * 邀请人Id
         */
        @NotBlank(message = "邀请人不能为空")
        String pid,

        /*
         * 订单来源Id
         */
        @NotBlank(message = "订单来源不能为空")
        String sourceId,
        
        /*
            备注
         */
        @NotBlank(message = "备注不能为空")
        String remark,

        /*
            商品Id
         */
        @NotBlank(message = "商品不能为空")
        String goodsId,

        /*
          商品数量
         */
        @NotNull(message = "商品数量不能为空")
        Integer goodsNum,

        /*
         * 收货人手机号
         */
        @ApiModelProperty(value = "收货人手机号", position = 5)
        @NotBlank(message = "收货人手机号不能为空")
        String mobile,

        /*
         * 收货人昵称
         */
        @ApiModelProperty(value = "收货人昵称", position = 6)
        String nickname,

        /*
         * 省
         */
        @ApiModelProperty(value = "省", position = 7)
        String provinceName,

        /*
         * 市
         */
        @ApiModelProperty(value = "市", position = 8)
        String cityName,

        /*
         * 县区
         */
        @ApiModelProperty(value = "县区", position = 9)
        String countiesName,

        /*
          详细地址
         */
        @ApiModelProperty(value = "详细地址", position = 10)
        String address
) {
}
