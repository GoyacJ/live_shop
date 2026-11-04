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
package vip.xiaonuo.shop.modular.mallgoods.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商城-商品编辑参数
 *
 * @author yuezisheng
 * @date  2025/02/20 15:25
 **/
@Getter
@Setter
public class MallGoodsEditParam {

    /** ID */
    @ApiModelProperty(value = "ID", required = true, position = 1)
    @NotBlank(message = "id不能为空")
    private String id;

    /** 商品标题 */
    @ApiModelProperty(value = "商品标题", position = 2)
    private String title;

    /** 商品详情 */
    @ApiModelProperty(value = "商品详情", position = 3)
    private String detail;

    /** 成本价 */
    @ApiModelProperty(value = "成本价", position = 4)
    private Double costPrice;

    /** 售价 */
    @ApiModelProperty(value = "售价", position = 5)
    private Double price;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 6)
    private String sort;

    /** 商品所属商家 */
    @ApiModelProperty(value = "商品所属商家", position = 7)
    private String merchant;

    /** 赠送的积分 */
    @ApiModelProperty(value = "赠送的积分", position = 8)
    private Integer giftPoints;

    /** 推广奖励积分 */
    @ApiModelProperty(value = "推广奖励积分", position = 9)
    private Integer spreadGift;

    /** 商品简述 */
    @ApiModelProperty(value = "商品简述", position = 10)
    private String description;

    /** 库存 */
    @ApiModelProperty(value = "库存", position = 11)
    private Integer stock;

    /** 库存预警值 */
    @ApiModelProperty(value = "库存预警值", position = 12)
    private Integer lowStock;

    /** 销量 */
    @ApiModelProperty(value = "销量", position = 13)
    private Integer sales;

    /** 虚拟销量 */
    @ApiModelProperty(value = "虚拟销量", position = 14)
    private Integer virtualSales;

    /** 状态 */
    @ApiModelProperty(value = "状态", position = 15)
    private String status;

    /** 主图 */
    @ApiModelProperty(value = "主图", position = 16)
    private String hostImage;

    /** 轮播图 */
    @ApiModelProperty(value = "轮播图", position = 16)
    private String carouseImage;


}
