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
package vip.xiaonuo.mobile.modular.manage.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import vip.xiaonuo.common.enums.SimpleStatus;
import vip.xiaonuo.integral.api.enums.IntegralChangeType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 反馈意见结果
 *
 *
 * @author goya*/
@Getter
@Setter
public class UserIntegralChangeVO implements Serializable {

    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "积分Id")
    private String integralId;

    @ApiModelProperty(value = "积分记录Id")
    private String integralRecordId;

    @ApiModelProperty(value = "积分来源")
    private String integralSource;

    @ApiModelProperty(value = "积分来源Id")
    private String integralSourceId;

    @ApiModelProperty("变动类型")
    private IntegralChangeType changeType;

    @ApiModelProperty("积分分类")
    private String integralCategory;

    @ApiModelProperty("操作时间")
    private LocalDateTime operationTime;

    @ApiModelProperty(value = "积分")
    private BigDecimal integral;

    @ApiModelProperty(value = "操作来源")
    private String operationSource;

    @ApiModelProperty(value = "操作人ID")
    private String operationUserId;

    @ApiModelProperty(value = "积分余额")
    private BigDecimal integralBalance;

    @ApiModelProperty(value = "冻结积分余额")
    private BigDecimal frozenIntegralBalance;

    @ApiModelProperty(value = "过期积分余额")
    private BigDecimal expireIntegralBalance;

    @ApiModelProperty(value = "过期积分清除状态")
    private SimpleStatus clearExpireStatus;

    @ApiModelProperty("积分有效期")
    private LocalDateTime validityPeriod;

    @ApiModelProperty(value = "备注")
    private String remark;

}
