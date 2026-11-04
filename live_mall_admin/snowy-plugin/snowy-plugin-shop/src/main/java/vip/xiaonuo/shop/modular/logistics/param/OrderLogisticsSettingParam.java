package vip.xiaonuo.shop.modular.logistics.param;

import org.noear.solon.validation.annotation.NotBlank;
import api.logistics.common.enums.Shipper;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/4/27 10:02
 */
public record OrderLogisticsSettingParam(
        /*
          快递公司编码
         */
        Shipper shipper,

        /*
          快递单号
         */
        @NotBlank(message = "快递单号不能为空")
        String logisticCode,

        /*
          手机尾号后四位
         */
        String customerName,

        /*
          订单编号
         */
        @NotBlank(message = "订单编号不能为空")
        String orderCode
) {
}
