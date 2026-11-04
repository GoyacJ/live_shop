package vip.xiaonuo.content.modular.action.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.pojo.CommonEntity;
import vip.xiaonuo.content.api.enums.ContentType;

/**
 * <p>统计信息表</p>
 *
 * @author goya
 * @since 2025/6/11 10:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("content_stat_counter")
public class ContentStatCounter extends CommonEntity {

    @Id
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("目标ID（视频/短剧）")
    private String contentId;

    @ApiModelProperty("目标类型（1-视频，2-短剧）")
    private ContentType contentType;

    @ApiModelProperty("观看次数")
    private Integer viewCount;

    @ApiModelProperty("点赞次数")
    private Integer likeCount;

    @ApiModelProperty("收藏次数")
    private Integer collectCount;

    @ApiModelProperty("分享次数")
    private Integer shareCount;

    @ApiModelProperty("评论次数")
    private Integer commentCount;

    @ApiModelProperty("追剧次数")
    private Integer followCount;

}
