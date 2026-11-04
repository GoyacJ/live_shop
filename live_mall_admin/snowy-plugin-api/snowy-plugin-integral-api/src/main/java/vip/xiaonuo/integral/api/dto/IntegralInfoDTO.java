package vip.xiaonuo.integral.api.dto;

import io.swagger.annotations.ApiModelProperty;
import vip.xiaonuo.common.enums.SimpleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/30 16:27
 */
public record IntegralInfoDTO(
        @ApiModelProperty(value = "ID")
        String id,

        @ApiModelProperty(value = "用户ID")
        String userId,

        @ApiModelProperty(value = "积分")
        BigDecimal integral,

        @ApiModelProperty(value = "冻结积分")
        BigDecimal frozenIntegral,

        @ApiModelProperty(value = "过期积分")
        BigDecimal expireIntegral,

        @ApiModelProperty(value = "最后修改时间")
        LocalDateTime lastModifyTime,

        @ApiModelProperty(value = "最后修改人ID")
        String lastModifyUserId,

        @ApiModelProperty(value = "积分状态")
        SimpleStatus status
) {
}
