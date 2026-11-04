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
package vip.xiaonuo.live.modular.spectatorwatching.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 直播观众观看时间表实体
 *
 * @author GMQ
 * @date  2025/04/06 13:10
 **/
@Getter
@Setter
@Table(value = "live_spectator_watching")
public class LiveSpectatorWatching {

    /** ID */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    /** 观众id */
    @ApiModelProperty(value = "观众id", position = 2)
    private String userId;

    /** 直播id */
    @ApiModelProperty(value = "直播id", position = 3)
    private String liveId;

    /** 开始时间 */
    @ApiModelProperty(value = "开始时间", position = 4)
    private Date startTime;

    /** 结束时间 */
    @ApiModelProperty(value = "结束时间", position = 5)
    private Date endTime;

    /** 删除标志 */
    @ApiModelProperty(value = "删除标志", position = 6)
    private String deleteFlag;
}
