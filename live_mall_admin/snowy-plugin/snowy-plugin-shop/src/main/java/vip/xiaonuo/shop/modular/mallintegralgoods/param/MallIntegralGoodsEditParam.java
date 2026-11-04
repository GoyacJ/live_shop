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
package vip.xiaonuo.shop.modular.mallintegralgoods.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 积分商城编辑参数
 *
 * @author 陈金坤
 * @date  2025/04/08 14:04
 **/
@Getter
@Setter
public class MallIntegralGoodsEditParam {

    /** ID */
    @ApiModelProperty(value = "ID", required = true, position = 1)
    @NotBlank(message = "id不能为空")
    private String id;

    /** 积分商品标题 */
    @ApiModelProperty(value = "积分商品标题", position = 2)
    private String title;

    /** 封面 */
    @ApiModelProperty(value = "封面", position = 3)
    private String cover;

    /** 市场价 */
    @ApiModelProperty(value = "市场价", position = 4)
    private String marketPrice;

    /** 兑换所需积分 */
    @ApiModelProperty(value = "兑换所需积分", position = 5)
    private Integer integral;

    /** 商品描述，纯文本 */
    @ApiModelProperty(value = "商品描述，纯文本", position = 6)
    private String detail;

    /** 库存 */
    @ApiModelProperty(value = "库存", position = 7)
    private Integer stock;

    /** 每人限购数量，0不限购，大于0为限购数量 */
    @ApiModelProperty(value = "每人限购数量，0不限购，大于0为限购数量", position = 8)
    private Integer userLimit;

    /** 状态，0禁用，1启用 */
    @ApiModelProperty(value = "状态，0禁用，1启用", position = 9)
    private String status;

}
