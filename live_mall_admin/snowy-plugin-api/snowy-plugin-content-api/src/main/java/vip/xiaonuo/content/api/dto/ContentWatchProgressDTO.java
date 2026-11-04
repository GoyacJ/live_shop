package vip.xiaonuo.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import vip.xiaonuo.content.api.enums.ContentType;

/**
 * <p>用户观看记录</p>
 *
 * @author goya
 * @since 2025/6/11 15:13
 */
public record ContentWatchProgressDTO(

        @ApiModelProperty("用户id")
        String userId,

        @ApiModelProperty("目标ID（视频/短剧）")
        String contentId,

        @ApiModelProperty("目标类型（1-视频，2-短剧）")
        ContentType contentType,

        @ApiModelProperty("剧集ID")
        String episodeId,

        @ApiModelProperty("观看进度（秒）")
        Integer progressSeconds,

        @ApiModelProperty("观看进度")
        String viewProgress

        ) {
}
