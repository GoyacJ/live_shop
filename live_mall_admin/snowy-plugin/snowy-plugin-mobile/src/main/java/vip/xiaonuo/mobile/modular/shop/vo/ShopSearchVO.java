package vip.xiaonuo.mobile.modular.shop.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/12 17:33
 */
@Data
public class ShopSearchVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 8075126862595040507L;

    private String goodsId;

    private String goodsImg;

    private String goodsTitle;

    private BigDecimal goodsPrice;

    private BigDecimal costPrice;
}
