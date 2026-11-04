package vip.xiaonuo.integral.modular.info.params;

import io.swagger.annotations.ApiModelProperty;
import org.noear.solon.validation.annotation.NotBlank;
import vip.xiaonuo.integral.api.enums.IntegralCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 16:34
 */
public record AdjustIntegralParam(

        @ApiModelProperty(value = "用户ID")
        @NotBlank(message = "用户Id不能为空")
        String userId,

        @ApiModelProperty(value = "积分")
        BigDecimal integral,

        @ApiModelProperty(value = "积分分类")
        IntegralCategory integralCategory,

        @ApiModelProperty("积分有效期")
        LocalDateTime validityPeriod,

        @ApiModelProperty(value = "备注")
        String remark
) {
}
