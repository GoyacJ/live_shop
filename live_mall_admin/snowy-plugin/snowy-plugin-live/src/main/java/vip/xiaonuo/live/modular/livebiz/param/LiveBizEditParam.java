/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.live.modular.livebiz.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;

import java.util.Date;

/**
 * 直播列表编辑参数
 *
 * @author zsj
 * @date  2025/04/02 16:57
 **/
@Getter
@Setter
public class LiveBizEditParam {

    /** ID */
    @ApiModelProperty(value = "ID", required = true, position = 1)
    @NotBlank(message = "id不能为空")
    private String id;

    /** 直播场次id */
    @ApiModelProperty(value = "直播场次id", position = 2)
    private Integer liveBroadcasts;

    /** 主播id */
    @ApiModelProperty(value = "主播id", position = 3)
    @NotNull(message = "主播不能为空")
    private Integer anchorId;

    /** 观众数量 */
    @ApiModelProperty(value = "观众数量", position = 4)
    private Integer spectatorNumber;

    /** 封面url */
    @ApiModelProperty(value = "封面url", position = 8)
    private String coverUrl;

    /** 直播间状态 0待开播 1启用 2禁用 */
    @ApiModelProperty(value = "直播间状态 0待开播 1启用 2禁用", position = 11)
    private String liveRoomState;

    /** 主标题 */
    @ApiModelProperty(value = "主标题", position = 12)
    @NotBlank(message = "标题不能为空")
    private String mainTitle;

    /** 直播类型 */
    @ApiModelProperty(value = "直播类型", position = 14)
    private String liveState;

    /** 直播开始时间 */
    @ApiModelProperty(value = "直播开始时间", position = 15)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "直播开始时间不能为空")
    private Date liveBeginTime;

    /** 直播结束时间 */
    @ApiModelProperty(value = "直播结束时间", position = 16)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "直播结束时间不能为空")
    private Date liveOverTime;

}
