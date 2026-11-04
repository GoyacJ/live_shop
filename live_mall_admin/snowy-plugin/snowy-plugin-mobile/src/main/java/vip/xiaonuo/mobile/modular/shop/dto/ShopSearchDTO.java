package vip.xiaonuo.mobile.modular.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/12 17:34
 */
public record ShopSearchDTO(
        String title,
        CommonSortOrderEnum sales,
        CommonSortOrderEnum price,
        CommonSortOrderEnum sort,
        @ApiModelProperty(value = "当前页码")
        Integer current,
        @ApiModelProperty(value = "每页条数")
        Integer size
) {
}
