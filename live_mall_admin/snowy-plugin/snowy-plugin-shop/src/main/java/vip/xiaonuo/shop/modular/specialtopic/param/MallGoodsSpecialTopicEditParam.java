package vip.xiaonuo.shop.modular.specialtopic.param;

import io.swagger.annotations.ApiModelProperty;
import org.noear.solon.validation.annotation.NotBlank;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/12 11:29
 */
public record MallGoodsSpecialTopicEditParam(

        @NotBlank(message = "ID不能为空")
        String id,

        @ApiModelProperty("主题名称")
        String name,

        @ApiModelProperty(value = "排序", position = 6)
        Integer sort,

        @ApiModelProperty("是否轮播 0是1否")
        Integer carousel,

        @ApiModelProperty("专题图片")
        String background
) {
}
