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
package vip.xiaonuo.shop.modular.mallorder.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.noear.solon.validation.annotation.NotBlank;

/**
 * 商城-订单编辑参数
 *
 **/
@Getter
@Setter
public class MallOrderEditParam {

    /** ID */
    @ApiModelProperty(value = "ID", required = true, position = 1)
    @NotBlank(message = "id不能为空")
    private String id;
    /** 订单价格 */
    @ApiModelProperty(value = "订单价格", position = 2)
    private Double orderFee;

    /** 物流编号 */
    @ApiModelProperty(value = "物流编号", position = 3)
    private String logisticsNumber;

    /** 物流状态 */
    @ApiModelProperty(value = "物流状态", position = 4)
    private String logisticsStatus;

    /** 手机号 */
    @ApiModelProperty(value = "手机号", position = 5)
    private String mobile;

    /** 收货人昵称 */
    @ApiModelProperty(value = "收货人昵称", position = 6)
    private String nickname;

    /** 省 */
    @ApiModelProperty(value = "省", position = 7)
    private String provinceName;

    /** 市 */
    @ApiModelProperty(value = "市", position = 8)
    private String cityName;

    /** 县区 */
    @ApiModelProperty(value = "县区", position = 9)
    private String countiesName;

    /** 详细地址 */
    @ApiModelProperty(value = "详细地址", position = 10)
    private String address;

    /** 状态 */
    @ApiModelProperty(value = "状态", position = 11)
    private String status;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 12)
    private String sort;

    /** 订单编号 */
    @ApiModelProperty(value = "订单编号", position = 12)
    private String orderNumber;


}
