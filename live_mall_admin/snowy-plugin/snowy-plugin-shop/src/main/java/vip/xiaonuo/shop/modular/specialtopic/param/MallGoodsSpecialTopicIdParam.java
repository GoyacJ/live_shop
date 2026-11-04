package vip.xiaonuo.shop.modular.specialtopic.param;

import io.swagger.annotations.ApiModelProperty;
import org.noear.solon.validation.annotation.NotBlank;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/12 11:29
 */
public record MallGoodsSpecialTopicIdParam(
        @ApiModelProperty(value = "ID", required = true)
        @NotBlank(message = "id不能为空")
        String id
) {
}
