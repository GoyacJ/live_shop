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
package vip.xiaonuo.manage.modular.userlevel.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 平台-用户等级编辑参数
 *
 * @author yuezisheng
 * @date  2025/02/18 09:16
 **/
@Getter
@Setter
public class UserLevelEditParam {

    /** ID */
    @ApiModelProperty(value = "ID", required = true, position = 1)
    @NotBlank(message = "id不能为空")
    private String id;

    /** 等级名称 */
    @ApiModelProperty(value = "等级名称", position = 2)
    private String name;

    /** 图表 */
    @ApiModelProperty(value = "图表", position = 3)
    private String icon;

    /** 升级条件-积分 */
    @ApiModelProperty(value = "升级条件-积分", position = 4)
    private Integer integralCondition;

    /** 升级条件-邀请人数 */
    @ApiModelProperty(value = "升级条件-邀请人数", position = 5)
    private Integer userCondition;

    /** 升级条件方式 */
    @ApiModelProperty(value = "升级条件方式", position = 6)
    private String conditionMode;

    /** 权益描述 */
    @ApiModelProperty(value = "权益描述", position = 7)
    private String equityIntroduction;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 8)
    private String sort;

    /** 状态 */
    @ApiModelProperty(value = "状态", position = 9)
    private String status;

}
