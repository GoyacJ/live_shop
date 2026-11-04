package vip.xiaonuo.shop.modular.specialtopic.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * <p>商品专题详情</p>
 *
 * @author goya
 * @since 2025/5/9 16:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("mall_goods_special_topic_detail")
public class MallGoodsSpecialTopicDetail extends CommonEntity {
    @Serial
    private static final long serialVersionUID = -5469513514924190560L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    @ApiModelProperty("专题Id")
    private String specialTopicId;

    @ApiModelProperty("商品Id")
    private String goodsId;

    @Column(ignore = true)
    private String title;

    @ApiModelProperty(value = "原价", position = 5)
    @Column(ignore = true)
    private BigDecimal price;

    @ApiModelProperty("商品数量")
    private Integer goodsNum;

    @ApiModelProperty("商品价格")
    private BigDecimal goodsPrice;

    @ApiModelProperty("商品图片")
    @Column(ignore = true)
    private String hostImage;
}
