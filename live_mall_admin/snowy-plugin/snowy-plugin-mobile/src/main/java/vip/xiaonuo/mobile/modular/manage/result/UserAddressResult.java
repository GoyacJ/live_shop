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
import org.noear.solon.validation.annotation.NotBlank;

import java.util.Date;

/**
 * 用户收货地址结果
 *
 **/
@Getter
@Setter
public class UserAddressResult {
    /** ID */
    @ApiModelProperty(value = "ID", required = true, position = 1)
    @NotBlank(message = "id不能为空")
    private String id;

    /** 手机号 */
    @ApiModelProperty(value = "手机号", position = 2)
    private String mobile;

    /** 昵称 */
    @ApiModelProperty(value = "昵称", position = 3)
    private String nickname;

    /** 省份 */
    @ApiModelProperty(value = "省份", position = 4)
    private String provinceName;
    /** 省份 */
    @ApiModelProperty(value = "省份", position = 4)
    private String provinceCode;

    /** 市 */
    @ApiModelProperty(value = "市", position = 4)
    private String cityName;
    /** 市 */
    @ApiModelProperty(value = "市", position = 4)
    private String cityCode;

    /** 区县 */
    @ApiModelProperty(value = "区县", position = 4)
    private String countiesName;
    /** 区县 */
    @ApiModelProperty(value = "区县", position = 4)
    private String countiesCode;

    /** 详细地址 */
    @ApiModelProperty(value = "详细地址", position = 4)
    private String address;

    /** 是否默认 */
    @ApiModelProperty(value = "是否默认", position = 5)
    private String isDefault;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 5)
    private Date createTime;
}
