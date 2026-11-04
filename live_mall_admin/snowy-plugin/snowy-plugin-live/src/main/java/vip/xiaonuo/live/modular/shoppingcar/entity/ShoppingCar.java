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
package vip.xiaonuo.live.modular.shoppingcar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 直播间购物车表实体
 *
 * @author lgq
 * @date 2025/03/04 14:17
 **/
@Getter
@Setter
@Table(value = "live_shopping_car")
public class ShoppingCar implements Serializable {

    @Serial
    private static final long serialVersionUID = -4331095864502837812L;
    /**
     * ID
     */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    @ApiModelProperty(value = "商品id", position = 2)
    private String goodsId;

    @ApiModelProperty(value = "商品", position = 3)
    @Column(ignore = true)
    private String goodsName;

    /**
     * 直播间id
     */
    @ApiModelProperty(value = "直播间id", position = 3)
    private String liveId;

    @ApiModelProperty(value = "直播间", position = 4)
    @Column(ignore = true)
    private String liveName;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量", position = 5)
    private Integer quantity;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", position = 12)
    private Integer sort;

    @ApiModelProperty(value = "小黄车标志 0是 1否", position = 12)
    private Integer yellowBikeState;

    @ApiModelProperty("小黄车时长 单位: 秒")
    @Column(ignore = true)
    private Integer yellowBikeSecond;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格", position = 6)
    private BigDecimal price;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", position = 7)
    private Date createTime;

    /**
     * 删除标志
     */
    @Column(isLogicDelete = true)
    @ApiModelProperty(value = "删除标志", position = 999)
    @JsonIgnore
    private String deleteFlag;
}
