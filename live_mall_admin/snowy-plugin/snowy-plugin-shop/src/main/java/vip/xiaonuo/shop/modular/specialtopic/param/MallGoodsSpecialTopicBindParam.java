package vip.xiaonuo.shop.modular.specialtopic.param;

import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/12 11:29
 */
public record MallGoodsSpecialTopicBindParam(
        String specialTopicId,
        String goodsId,
        Integer goodsNum,
        BigDecimal goodsPrice
) {
}
