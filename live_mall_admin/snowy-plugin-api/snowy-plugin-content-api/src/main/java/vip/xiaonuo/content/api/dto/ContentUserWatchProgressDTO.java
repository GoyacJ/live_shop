package vip.xiaonuo.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>用户观看记录</p>
 *
 * @author goya
 * @since 2025/6/11 15:13
 */
@Data
public class ContentUserWatchProgressDTO implements Serializable {

    @ApiModelProperty(value = "观看进度")
    private String viewProgress;

    @ApiModelProperty(value = "最后观看时间")
    private LocalDateTime lastWatchTime;
}
