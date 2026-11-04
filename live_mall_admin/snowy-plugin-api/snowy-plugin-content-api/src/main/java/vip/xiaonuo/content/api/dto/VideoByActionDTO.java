package vip.xiaonuo.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 14:03
 */
@Data
public class VideoByActionDTO implements Serializable {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "分类")
    private String category;

    @ApiModelProperty(value = "标签")
    private String tags;

    @ApiModelProperty(value = "封面图")
    private String cover;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "视频地址")
    private String videoUrl;

    @ApiModelProperty(value = "视频时长（秒）")
    private Integer duration;

    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty(value = "是否可下载 0是1否")
    private Integer downloadable;

    @ApiModelProperty(value = "是否可分享 0是1否")
    private Integer shareable;

    @ApiModelProperty(value = "是否可评论 0是1否")
    private Integer allowComment;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "商品基本信息")
    private GoodsSampleInfoDTO goodsSampleInfo;

    @ApiModelProperty(value = "用户动作")
    private ContentUserActionInfoDTO userAction;

    @ApiModelProperty(value = "内容统计信息")
    private ContentStatsDTO contentStats;

    @ApiModelProperty(value = "用户观看进度")
    private ContentUserWatchProgressDTO userWatchProgress;
}
