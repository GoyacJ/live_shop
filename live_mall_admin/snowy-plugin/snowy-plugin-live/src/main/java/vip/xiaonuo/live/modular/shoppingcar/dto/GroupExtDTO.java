package vip.xiaonuo.live.modular.shoppingcar.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/12 15:40
 */
@Data
public class GroupExtDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 591807297178395552L;

    private String state;
    private Top top;
    private Tips tips;


    @Data
    public static class Top implements Serializable{

        @Serial
        private static final long serialVersionUID = -5113067672603771610L;

        @ApiModelProperty(value = "ID", position = 1)
        private String shoppingCarId;

        @ApiModelProperty(value = "商品id", position = 2)
        private String goodsId;

        @ApiModelProperty(value = "商品", position = 3)
        private String goodsName;

        @ApiModelProperty(value = "直播间id", position = 3)
        private String liveId;

        @ApiModelProperty(value = "价格", position = 6)
        private BigDecimal price;

        @ApiModelProperty(value = "主图", position = 16)
        private String hostImage;
    }


    @Data
    public static class Tips implements Serializable{

        @Serial
        private static final long serialVersionUID = 6114516581510462515L;

        @ApiModelProperty(value = "ID", position = 1)
        private String shoppingCarId;

        @ApiModelProperty(value = "商品id", position = 2)
        private String goodsId;

        @ApiModelProperty(value = "商品", position = 3)
        private String goodsName;

        @ApiModelProperty(value = "直播间id", position = 3)
        private String liveId;

        @ApiModelProperty(value = "价格", position = 6)
        private BigDecimal price;

        @ApiModelProperty(value = "主图", position = 16)
        private String hostImage;

        @ApiModelProperty("小黄车时长")
        private Integer yellowBikeSecond;

        @ApiModelProperty("小黄车截止时间")
        private LocalDateTime yellowBikeEndTime;

    }
}
