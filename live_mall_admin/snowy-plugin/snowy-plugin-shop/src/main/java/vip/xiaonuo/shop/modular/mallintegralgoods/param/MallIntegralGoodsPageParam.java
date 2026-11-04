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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 积分商城查询参数
 *
 * @author 陈金坤
 * @date  2025/04/08 14:04
 **/
@Getter
@Setter
public class MallIntegralGoodsPageParam {

    /** 当前页 */
    @ApiModelProperty(value = "当前页码")
    private Integer current;

    /** 每页条数 */
    @ApiModelProperty(value = "每页条数")
    private Integer size;

    /** 排序字段 */
    @ApiModelProperty(value = "排序字段，字段驼峰名称，如：userName")
    private String sortField;

    /** 排序方式 */
    @ApiModelProperty(value = "排序方式，升序：ASCEND；降序：DESCEND")
    private String sortOrder;

    /** 关键词 */
    @ApiModelProperty(value = "关键词")
    private String searchKey;

    /** 积分商品标题 */
    @ApiModelProperty(value = "积分商品标题")
    private String title;

    /** 市场价 */
    @ApiModelProperty(value = "市场价")
    private String marketPrice;

    @ApiModelProperty(value = "最低市场价")
    private BigDecimal minMarketPrice;

    @ApiModelProperty(value = "最高市场价")
    private BigDecimal maxMarketPrice;

    /** 兑换所需积分 */
    @ApiModelProperty(value = "兑换所需积分")
    private Integer integral;

    /** 商品描述，纯文本 */
    @ApiModelProperty(value = "商品描述，纯文本")
    private String detail;

    /** 状态，0禁用，1启用 */
    @ApiModelProperty(value = "状态，0禁用，1启用")
    private String status;

}
