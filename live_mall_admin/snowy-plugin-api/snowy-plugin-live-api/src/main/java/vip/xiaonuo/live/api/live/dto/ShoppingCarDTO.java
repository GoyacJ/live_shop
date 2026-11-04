package vip.xiaonuo.live.api.live.dto;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/7 14:46
 */
@Data
public class ShoppingCarDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8612040079383883697L;

    /** ID */
    @Id
    @ApiModelProperty(value = "ID", position = 1)
    private String id;

    @ApiModelProperty(value = "商品id", position = 2)
    private String goodsId;

    @ApiModelProperty(value = "商品", position = 3)
    @Column(ignore = true)
    private String goodsName;

    /** 直播间id */
    @ApiModelProperty(value = "直播间id", position = 3)
    private String liveId;

    @ApiModelProperty(value = "直播间", position = 4)
    @Column(ignore = true)
    private String liveName;

    /** 数量 */
    @ApiModelProperty(value = "数量", position = 5)
    private Integer quantity;

    /** 价格 */
    @ApiModelProperty(value = "价格", position = 6)
    private BigDecimal price;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 7)
    private Date createTime;

}
