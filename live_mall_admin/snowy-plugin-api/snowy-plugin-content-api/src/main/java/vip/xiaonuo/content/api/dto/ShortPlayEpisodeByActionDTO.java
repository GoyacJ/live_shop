package vip.xiaonuo.content.api.dto;

import com.mybatisflex.annotation.Id;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/14 15:50
 */
@Data
public class ShortPlayEpisodeByActionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 8346182125846651925L;

    @Id
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "短剧ID")
    private String shortPlayId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "剧集编号")
    private Integer episodeNumber;

    @ApiModelProperty(value = "封面图")
    private String cover;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否免费")
    private Boolean isFree;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "视频信息")
    private VideoByActionDTO videoByAction;

    @ApiModelProperty("商品Id")
    private String goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty(value = "价格", position = 6)
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "主图", position = 16)
    private String hostImage;

    @ApiModelProperty("展示商品开始秒数")
    private Integer startShowGoodsSecond;

    @ApiModelProperty("展示商品秒数")
    private Integer showGoodsSecond;

    @ApiModelProperty(value = "总集数")
    private Integer episodeCount;
}
