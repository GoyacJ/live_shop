package vip.xiaonuo.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import vip.xiaonuo.content.api.enums.ContentType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/11 14:32
 */
@Data
public class ContentWatchHistoryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("内容类型")
    private ContentType contentType;

    @ApiModelProperty("内容id")
    private String contentId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("剧集id")
    private String episodeId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("封面图")
    private String cover;

    @ApiModelProperty("视频时长（秒）")
    private Integer duration;

    @ApiModelProperty(value = "剧集编号")
    private Integer episodeNumber;

    @ApiModelProperty("视频地址")
    private String videoUrl;

    @ApiModelProperty("观看进度")
    private String viewProgress;

    @ApiModelProperty("最后观看时间")
    private LocalDateTime lastWatchTime;

    @ApiModelProperty("是否追剧")
    private Boolean isFollow;
}
