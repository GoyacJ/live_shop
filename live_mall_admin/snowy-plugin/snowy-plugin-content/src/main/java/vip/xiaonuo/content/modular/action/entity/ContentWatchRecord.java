package vip.xiaonuo.content.modular.action.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.pojo.CommonEntity;
import vip.xiaonuo.content.api.enums.ContentType;

import java.time.LocalDateTime;

/**
 * <p>观看记录表</p>
 *
 * @author goya
 * @since 2025/6/11 11:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("content_watch_record")
public class ContentWatchRecord extends CommonEntity {

    @Id
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("目标ID（视频/短剧）")
    private String contentId;

    @ApiModelProperty("目标类型（1-视频，2-短剧）")
    private ContentType contentType;

    @ApiModelProperty("剧集ID")
    private String episodeId;

    @ApiModelProperty("观看进度（秒）")
    private Integer progressSeconds;

    @ApiModelProperty("观看进度")
    private String viewProgress;

    @ApiModelProperty("开始观看时间")
    private LocalDateTime startWatchTime;

    @ApiModelProperty("最后观看时间")
    private LocalDateTime lastWatchTime;
}
