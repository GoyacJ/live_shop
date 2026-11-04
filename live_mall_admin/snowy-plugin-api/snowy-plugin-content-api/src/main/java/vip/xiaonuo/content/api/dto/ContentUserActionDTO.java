package vip.xiaonuo.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.content.api.enums.ContentActionType;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 13:34
 */
public record ContentUserActionDTO(
        @ApiModelProperty("用户id")
        @NotBlank(message = "用户id不能为空")
        String userId,

        @ApiModelProperty("视频Id")
        @NotBlank(message = "视频Id不能为空")
        String videoId,

        @ApiModelProperty("操作类型")
        @NotNull(message = "操作类型不能为空")
        ContentActionType contentActionType,

        @ApiModelProperty("是否生效")
        @NotNull(message = "操作类型不能为空")
        SimpleStatus isActive
        ) {
}
