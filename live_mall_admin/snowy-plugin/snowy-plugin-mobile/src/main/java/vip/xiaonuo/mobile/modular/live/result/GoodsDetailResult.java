package vip.xiaonuo.mobile.modular.live.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/15 17:15
 * Description: 商品详情
 */
@Getter
@Setter
public class GoodsDetailResult {
    @ApiModelProperty(value = "商品ID", position = 1)
    private String id;
    @ApiModelProperty(value = "商品标题", position = 3)
    private String title;
    @ApiModelProperty(value = "商品详情", position = 4)
    private String detail;
    @ApiModelProperty(value = "市场价格", position = 5)
    private String costPrice;
    @ApiModelProperty
    private String price;
    @ApiModelProperty(value = "轮播图", position = 7)
    private List<String> slider;
    @ApiModelProperty(value = "销量", position = 8)
    private int sales;
    /** 主图 */
    @ApiModelProperty(value = "主图", position = 16)
    private String hostImage;
}
