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
package vip.xiaonuo.live.modular.livebiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 直播列表实体
 *
 * @author zsj
 * @date  2025/04/02 16:57
 **/
@Getter
@Setter
@Table(value = "live_biz")
public class LiveBiz extends CommonEntity {

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
