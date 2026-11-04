package vip.xiaonuo.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 14:25
 */
public record ShortPlayByActionPageDTO(

        @ApiModelProperty(value = "标题")
        String title,

        @ApiModelProperty(value = "用户Id")
        String userId,

        @ApiModelProperty(value = "查询数量")
        Integer total,

        @ApiModelProperty(value = "排序")
        CommonSortOrderEnum sort,

        @ApiModelProperty(value = "当前页码")
        Integer current,

        @ApiModelProperty(value = "每页条数")
        Integer size
) {
}
