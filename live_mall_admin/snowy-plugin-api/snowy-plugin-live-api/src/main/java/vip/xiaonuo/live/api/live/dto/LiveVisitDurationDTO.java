package vip.xiaonuo.live.api.live.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/6 14:19
 */
@Data
public class LiveVisitDurationDTO implements Serializable {

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("直播Id")
    private String liveId;

    @ApiModelProperty("视频时长（秒）")
    private Integer duration;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
}
