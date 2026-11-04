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
package vip.xiaonuo.manage.modular.userinfo.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户列表添加参数
 *
 * @author zhaishaojiang
 * @date  2025/02/17 17:28
 **/
@Getter
@Setter
public class UserInfoAddParam {

    /** 手机号 */
    @ApiModelProperty(value = "手机号", position = 2)
    private String mobile;

    /** 头像 */
    @ApiModelProperty(value = "头像", position = 3)
    private String avatar;

    /** 等级id */
    @ApiModelProperty(value = "等级id", position = 4)
    private String levelId;

    /** 昵称 */
    @ApiModelProperty(value = "昵称", position = 5)
    private String nickname;

    /** 积分账户 */
    @ApiModelProperty(value = "积分账户", position = 6)
    private BigDecimal integral;

    /** 冻结积分 */
    @ApiModelProperty(value = "冻结积分", position = 7)
    private BigDecimal frozenIntegral;

    /** 现金账户 */
    @ApiModelProperty(value = "现金账户", position = 8)
    private Double cash;

    /** 冻结现金 */
    @ApiModelProperty(value = "冻结现金", position = 9)
    private Double frozenCash;

    /** 上次登录ip */
    @ApiModelProperty(value = "上次登录ip", position = 10)
    private String lastLoginIp;

    /** 上次登录地点 */
    @ApiModelProperty(value = "上次登录地点", position = 11)
    private String lastLoginAddress;

    /** 上次登录时间 */
    @ApiModelProperty(value = "上次登录时间", position = 12)
    private Date lastLoginTime;

    /** 上次登录设备 */
    @ApiModelProperty(value = "上次登录设备", position = 13)
    private String lastLoginDevice;

    /** 用户状态 */
    @ApiModelProperty(value = "用户状态", position = 14)
    private String userStatus;

    /** 邀请人id */
    @ApiModelProperty(value = "邀请人id", position = 15)
    private String pId;

    /** 用户邀请码 */
    @ApiModelProperty(value = "用户邀请码", position = 16)
    private String rcode;

}
