package vip.xiaonuo.pay.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/6/23 09:25
 */
public record CreateH5PayOrderDTO(

        // 订单号
        String orderNo,

        // 订单描述
        String description,

        // 扩展信息
        String attach,
        // 订单总金额 元
        BigDecimal total
) implements Serializable {

}
