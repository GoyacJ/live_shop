package vip.xiaonuo.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/14 15:18
 */
public record VideoByActionPageDTO(

        @ApiModelProperty(value = "标题")
        String title,

        @ApiModelProperty(value = "用户Id")
        String userId,

        @ApiModelProperty(value = "排序")
        CommonSortOrderEnum sort,

        @ApiModelProperty(value = "当前页码")
        Integer current,

        @ApiModelProperty(value = "每页条数")
        Integer size
) {
}
