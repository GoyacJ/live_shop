package api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import vip.xiaonuo.common.enums.CommonSortOrderEnum;

/**
 * 商城-商品添加参数
 *
 * @author goya
 * @date 2025/02/20 15:25
 **/
public record MallGoodsSearchDTO(
        @ApiModelProperty(value = "商品标题")
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
