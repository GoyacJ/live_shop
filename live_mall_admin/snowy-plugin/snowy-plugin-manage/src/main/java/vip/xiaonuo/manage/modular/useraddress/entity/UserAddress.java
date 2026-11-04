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
package vip.xiaonuo.manage.modular.useraddress.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商城-用户收获地址实体
 *
 * @author yuezisheng
 * @date  2025/02/18 21:28
 **/
@Getter
@Setter
@Table(value = "user_address")
public class UserAddress extends CommonEntity {

    /** ID */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    /** 联系方式 */
    @ApiModelProperty(value = "联系方式", position = 2)
    private String mobile;

    /** 收货人昵称 */
    @ApiModelProperty(value = "收货人昵称", position = 3)
    private String nickname;

    /** 省份 */
    @ApiModelProperty(value = "省份", position = 4)
    private String provinceName;
    /** 省份 */
    @ApiModelProperty(value = "省份", position = 4)
    private String provinceCode;

    /** 市 */
    @ApiModelProperty(value = "市", position = 5)
    private String cityName;
    /** 市 */
    @ApiModelProperty(value = "市", position = 5)
    private String cityCode;

    /** 区县 */
    @ApiModelProperty(value = "区县", position = 6)
    private String countiesName;
    /** 区县 */
    @ApiModelProperty(value = "区县", position = 6)
    private String countiesCode;

    /** 详细地址 */
    @ApiModelProperty(value = "详细地址", position = 7)
    private String address;

    /** 是否为默认地址 */
    @ApiModelProperty(value = "是否为默认地址", position = 8)
    private String isDefault;


}
