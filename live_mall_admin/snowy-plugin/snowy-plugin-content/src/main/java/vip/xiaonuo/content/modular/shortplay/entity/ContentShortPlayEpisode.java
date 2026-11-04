package vip.xiaonuo.content.modular.shortplay.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.io.Serial;

/**
 * <p>短剧集</p>
 *
 * @author goya
 * @since 2025/5/14 13:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("content_short_play_episode")
public class ContentShortPlayEpisode extends CommonEntity {
    @Serial
    private static final long serialVersionUID = -2315956830753653269L;

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

    @ApiModelProperty(value = "视频ID")
    private String videoId;

    @ApiModelProperty(value = "视频地址")
    private String videoUrl;

    @ApiModelProperty(value = "视频时长（秒）")
    private Integer duration;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否免费")
    private Boolean isFree;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty("商品Id")
    private String goodsId;

    @ApiModelProperty("展示商品开始秒数")
    private Integer startShowGoodsSecond;

    @ApiModelProperty("展示商品秒数")
    private Integer showGoodsSecond;
}
