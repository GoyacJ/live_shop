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
package vip.xiaonuo.mobile.api.auth.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 移动端登录用户对象
 *
 */
@Getter
@Setter
public abstract class MobileLoginUser implements Serializable {

    /** id */
    @ApiModelProperty(value = "id", position = 1)
    private String id;

    /** 头像 */
    @ApiModelProperty(value = "头像", position = 3)
    private String avatar;

    /** 手机号 */
    @ApiModelProperty(value = "手机号", position = 4)
    private String mobile;

    /** 等级id */
    @ApiModelProperty(value = "等级id", position = 5)
    private String levelId;

    /** 账户积分 */
    @ApiModelProperty(value = "账户积分", position = 6)
    private String integral;

    /** 昵称 */
    @ApiModelProperty(value = "昵称", position = 7)
    private String nickname;

    /** 冻结积分 */
    @ApiModelProperty(value = "冻结积分", position = 8)
    private String frozenIntegral;

    /** 现金账户 */
    @ApiModelProperty(value = "现金账户", position = 9)
    private String cash;

    /** 冻结现金 */
    @ApiModelProperty(value = "冻结现金", position = 10)
    private String frozenCash;

    /** 上次登录ip */
    @ApiModelProperty(value = "上次登录ip", position = 39)
    private String lastLoginIp;

    /** 上次登录地点 */
    @ApiModelProperty(value = "上次登录地点", position = 40)
    private String lastLoginAddress;

    /** 上次登录时间 */
    @ApiModelProperty(value = "上次登录时间", position = 41)
    private Date lastLoginTime;

    /** 上次登录设备 */
    @ApiModelProperty(value = "上次登录设备", position = 42)
    private String lastLoginDevice;

    /** 最新登录ip */
    @ApiModelProperty(value = "最新登录ip", position = 43)
    private String latestLoginIp;

    /** 最新登录地点 */
    @ApiModelProperty(value = "最新登录地点", position = 44)
    private String latestLoginAddress;

    /** 最新登录时间 */
    @ApiModelProperty(value = "最新登录时间", position = 45)
    private Date latestLoginTime;

    /** 最新登录设备 */
    @ApiModelProperty(value = "最新登录设备", position = 46)
    private String latestLoginDevice;

    /** 用户状态 */
    @ApiModelProperty(value = "用户状态", position = 47)
    private String userStatus;

    /** 排序码 */
    @ApiModelProperty(value = "排序码", position = 48)
    private Integer sortCode;

    /** 扩展信息 */
    @ApiModelProperty(value = "扩展信息", position = 49)
    private String extJson;

}
