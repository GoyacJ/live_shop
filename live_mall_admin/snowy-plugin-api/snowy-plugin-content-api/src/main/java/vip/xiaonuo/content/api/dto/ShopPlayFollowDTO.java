package vip.xiaonuo.content.api.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 13:29
 */
public record ShopPlayFollowDTO(

        @ApiModelProperty("用户Id")
        String userId,

        @ApiModelProperty("短剧Id")
        String shopPlayId,

        @ApiModelProperty("是否追剧")
        Boolean isFollow
) {
}
