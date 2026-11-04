package vip.xiaonuo.integral.api.dto;

import io.swagger.annotations.ApiModelProperty;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;
import vip.xiaonuo.integral.api.enums.IntegralCategory;
import vip.xiaonuo.integral.api.enums.IntegralChangeType;
import vip.xiaonuo.integral.api.enums.IntegralSource;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/16 15:13
 */
public record UserIntegralChangePageDTO(

        @ApiModelProperty(value = "变更类型")
        IntegralChangeType integralChangeType,

        @ApiModelProperty(value = "积分来源")
        IntegralSource integralSource,

        @ApiModelProperty(value = "积分类型")
        IntegralCategory integralCategory,

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
