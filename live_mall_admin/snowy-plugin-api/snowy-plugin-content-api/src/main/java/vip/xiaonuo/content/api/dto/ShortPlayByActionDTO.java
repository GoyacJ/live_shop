package vip.xiaonuo.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 13:51
 */
@Data
public class ShortPlayByActionDTO implements Serializable {

    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "分类")
    private String category;

    @ApiModelProperty(value = "标签列表")
    private String tags;

    @ApiModelProperty(value = "总集数")
    private Integer episodeCount;

    @ApiModelProperty(value = "封面图")
    private String cover;

    @ApiModelProperty(value = "海报图")
    private String poster;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "推荐等级（1-普通，2-推荐，3-强推等）")
    private Integer recommendLevel;

    @ApiModelProperty(value = "发布时间")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "是否允许评论")
    private Boolean allowComment;

    @ApiModelProperty(value = "是否允许分享")
    private Boolean allowShare;

    @ApiModelProperty(value = "是否免费")
    private Boolean isFree;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "是否追剧")
    private Boolean isFollow;

    @ApiModelProperty(value = "最后观看剧集No")
    private Integer lastWatchEpisodeNo;

    @ApiModelProperty(value = "统计信息")
    private ContentStatsDTO contentStats;
}
