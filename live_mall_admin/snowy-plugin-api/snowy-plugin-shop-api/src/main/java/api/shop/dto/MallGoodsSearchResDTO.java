package api.shop.dto;

import com.mybatisflex.annotation.Id;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/12 17:44
 */
@Data
public class MallGoodsSearchResDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6270906907300611232L;

    /** ID */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    /** 商品标题 */
    @ApiModelProperty(value = "商品标题", position = 2)
    private String title;

    /** 商品详情 */
    @ApiModelProperty(value = "商品详情", position = 3)
    private String detail;

    /** 成本价 */
    @ApiModelProperty(value = "成本价", position = 4)
    private Double costPrice;

    /** 售价 */
    @ApiModelProperty(value = "售价", position = 5)
    private Double price;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 6)
    private String sort;

    /** 商品所属商家 */
    @ApiModelProperty(value = "商品所属商家", position = 7)
    private String merchant;

    /** 赠送的积分 */
    @ApiModelProperty(value = "赠送的积分", position = 8)
    private Integer giftPoints;

    /** 推广奖励积分 */
    @ApiModelProperty(value = "推广奖励积分", position = 9)
    private Integer spreadGift;

    /** 商品简述 */
    @ApiModelProperty(value = "商品简述", position = 10)
    private String description;

    /** 库存 */
    @ApiModelProperty(value = "库存", position = 11)
    private Integer stock;

    /** 库存预警值 */
    @ApiModelProperty(value = "库存预警值", position = 12)
    private Integer lowStock;

    /** 销量 */
    @ApiModelProperty(value = "销量", position = 13)
    private Integer sales;

    /** 虚拟销量 */
    @ApiModelProperty(value = "虚拟销量", position = 14)
    private Integer virtualSales;

    /** 状态 */
    @ApiModelProperty(value = "状态", position = 15)
    private String status;

    /** 主图 */
    @ApiModelProperty(value = "主图", position = 16)
    private String hostImage;

    /** 轮播图 */
    @ApiModelProperty(value = "轮播图", position = 16)
    private String carouseImage;
}
