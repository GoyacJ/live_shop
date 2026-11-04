package vip.xiaonuo.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/12 13:37
 */
@Data
public class GoodsSampleInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品Id")
    private String goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品价格")
    private BigDecimal goodsPrice;

    @ApiModelProperty("商品图片")
    private String hostImage;

    @ApiModelProperty("展示商品开始秒数")
    private Integer startShowGoodsSecond;

    @ApiModelProperty("展示商品秒数")
    private Integer showGoodsSecond;

}
