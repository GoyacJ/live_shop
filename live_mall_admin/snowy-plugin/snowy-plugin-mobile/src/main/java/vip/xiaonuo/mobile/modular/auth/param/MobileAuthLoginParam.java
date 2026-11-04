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
package vip.xiaonuo.mobile.modular.auth.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.noear.solon.validation.annotation.NotBlank;


/**
 * 账号密码登录参数
 **/
@Getter
@Setter
public class MobileAuthLoginParam {

    /** 手机号 */
    @ApiModelProperty(value = "手机号", required = true, position = 1)
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    /** 验证码 */
    @ApiModelProperty(value = "验证码", required = true, position = 2)
    @NotBlank(message = "验证码不能为空")
    private String validCode;

    /** 用户邀请码 */
    @ApiModelProperty(value = "用户邀请码", position = 3)
    private String rcode;

    /** 用户设备Id */
    @ApiModelProperty(value = "用户设备Id",position = 4)
    @NotBlank(message = "用户设备Id不能为空")
    private String clientId;

}
