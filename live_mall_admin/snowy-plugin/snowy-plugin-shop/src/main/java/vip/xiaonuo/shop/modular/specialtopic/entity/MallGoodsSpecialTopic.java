package vip.xiaonuo.shop.modular.specialtopic.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.common.pojo.CommonEntity;

import java.io.Serial;

/**
 * <p>商品专题表/p>
 *
 * @author goya
 * @since 2025/5/9 15:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("mall_goods_special_topic")
public class MallGoodsSpecialTopic extends CommonEntity {

    @Serial
    private static final long serialVersionUID = -1761790739779144359L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty("主题名称")
    private String name;

    @ApiModelProperty("商品数量")
    private Integer goodsNum;

    /** 排序 */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty("是否轮播 0是1否")
    private Integer carousel;

    @ApiModelProperty("专题图片")
    private String background;

}
