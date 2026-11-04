package vip.xiaonuo.live.api.live.dto;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/7 16:08
 */
@Data
public class LiveBizDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4031380037621820201L;


    /** ID */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    /** 直播场次id */
    @ApiModelProperty(value = "直播场次id", position = 2)
    private Integer liveBroadcasts;

    /** 主播id */
    @ApiModelProperty(value = "主播id", position = 3)
    private Integer anchorId;

    @ApiModelProperty(value = "主播姓名", position = 3)
    @Column(ignore = true)
    private String anchorName;

    /** 观众数量 */
    @ApiModelProperty(value = "观众数量", position = 4)
    private Integer spectatorNumber;

    /** 推流url */
    @ApiModelProperty(value = "推流url", position = 5)
    private String ingestUrl;

    /** 观看url */
    @ApiModelProperty(value = "观看url", position = 6)
    private String liveUrl;

    /** 头像url */
    @ApiModelProperty(value = "头像url", position = 7)
    private String avatarUrl;

    /** 封面url */
    @ApiModelProperty(value = "封面url", position = 8)
    private String coverUrl;

    /** 回放url */
    @ApiModelProperty(value = "回放url", position = 9)
    private String backUrl;

    /** 是否录屏 0录屏 1不录 */
    @ApiModelProperty(value = "是否录屏 0录屏 1不录", position = 10)
    private Boolean isScreenRecording;

    /** 直播间状态 0待开播 1启用 2禁用 */
    @ApiModelProperty(value = "直播间状态 0待开播 1启用 2禁用", position = 11)
    private String liveRoomState;

    /** 主标题 */
    @ApiModelProperty(value = "主标题", position = 12)
    private String mainTitle;

    /** 子标题 */
    @ApiModelProperty(value = "子标题", position = 13)
    private String subTitle;

    /** 直播类型 */
    @ApiModelProperty(value = "直播类型", position = 14)
    private String liveState;

    @ApiModelProperty(value = "主播姓名", position = 3)
    @Column(ignore = true)
    private String liveStateName;

    /** 直播开始时间 */
    @ApiModelProperty(value = "直播开始时间", position = 15)
    private Date liveBeginTime;

    /** 直播结束时间 */
    @ApiModelProperty(value = "直播结束时间", position = 16)
    private Date liveOverTime;
}
