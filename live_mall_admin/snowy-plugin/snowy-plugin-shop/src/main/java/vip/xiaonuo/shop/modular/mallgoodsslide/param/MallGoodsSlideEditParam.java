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
package vip.xiaonuo.shop.modular.mallgoodsslide.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品轮播图编辑参数
 *
 * @author yuezisheng
 * @date  2025/02/20 15:30
 **/
@Getter
@Setter
public class MallGoodsSlideEditParam {

    /** ID */
    @ApiModelProperty(value = "ID", required = true, position = 1)
    @NotBlank(message = "id不能为空")
    private String id;

    /** 商品id */
    @ApiModelProperty(value = "商品id", position = 2)
    private String goodsId;

    /** 商品图片链接 */
    @ApiModelProperty(value = "商品图片链接", position = 3)
    private String url;

    /** 类型，1=图片/2=视频 */
    @ApiModelProperty(value = "类型，1=图片/2=视频", position = 4)
    private String type;

    /** 排序，从大到小 */
    @ApiModelProperty(value = "排序，从大到小", position = 5)
    private String sort;

    /** 状态，1=启用/2=禁用 */
    @ApiModelProperty(value = "状态，1=启用/2=禁用", position = 6)
    private String status;

}
