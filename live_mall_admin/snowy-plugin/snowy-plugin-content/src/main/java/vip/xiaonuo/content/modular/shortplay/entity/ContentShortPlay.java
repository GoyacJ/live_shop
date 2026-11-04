package vip.xiaonuo.content.modular.shortplay.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/13 13:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("content_short_play")
public class ContentShortPlay extends CommonEntity {
    @Serial
    private static final long serialVersionUID = 9131062847631651807L;

    @Id
    @ApiModelProperty(value = "ID")
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

    @ApiModelProperty(value = "状态（0-未发布，1-已发布）")
    private Integer status;

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
}
