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
package vip.xiaonuo.mobile.modular.order.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Author: shaojiang
 * CreateTime: 2025/4/7 16:42
 * Description: 积分商城列表返回结果
 */
@Getter
@Setter
public class IntegralGoodsDetailResult {
    /** 主键 */
    @ApiModelProperty(value = "主键", position = 1)
    private String id;
    /** 商品标题 */
    @ApiModelProperty(value = "商品标题", position = 2)
    private String title;
    /** 封面图片地址 */
    @ApiModelProperty(value = "封面图片地址", position = 3)
    private String cover;
    /** 市场价 */
    @ApiModelProperty(value = "市场价", position = 3)
    private BigDecimal marketPrice;
    /** 兑换所需积分 */
    @ApiModelProperty(value = "兑换所需积分", position = 3)
    private Long integral;
    /** 库存 */
    @ApiModelProperty(value = "库存", position = 3)
    private Long stock;
    /** 详情 */
    @ApiModelProperty(value = "详情", position = 3)
    private String detail;
}
