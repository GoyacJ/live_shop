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
package vip.xiaonuo.live.modular.anchor.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 主播信息表实体
 *
 * @author lgq
 * @date  2025/03/21 14:22
 **/
@Getter
@Setter
@Table(value = "live_anchor")
public class Anchor {

    /** ID */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private Integer id;

    /** 主播name */
    @ApiModelProperty(value = "主播name", position = 2)
    private String username;

    /** 昵称 */
    @ApiModelProperty(value = "昵称", position = 3)
    private String realName;

    /** 账号 */
    @ApiModelProperty(value = "账号", position = 4)
    private String account;

    /** 密码 */
    @ApiModelProperty(value = "密码", position = 5)
    private String password;

    /** 头像url */
    @ApiModelProperty(value = "头像url", position = 6)
    private String pictureUrl;

    /** 简介 */
    @ApiModelProperty(value = "简介", position = 7)
    private String bio;
}
