package vip.xiaonuo.mobile.modular.integral.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/28 19:07
 */
public record SharingLinksDTO(

        @ApiModelProperty("用户Id")
        String userId,

        @ApiModelProperty("业务Id")
        String businessId,

        @ApiModelProperty("分享类型")
        String category
) {
}
