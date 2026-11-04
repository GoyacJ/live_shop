package vip.xiaonuo.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 14:04
 */
@Data
public class ContentStatsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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

    public ContentStatsDTO() {
        this.viewCount = 0;
        this.likeCount = 0;
        this.collectCount = 0;
        this.commentCount = 0;
        this.shareCount = 0;
        this.followCount = 0;
    }
}
