package vip.xiaonuo.shop.modular.logistics.dto;

import api.logistics.common.enums.Shipper;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import vip.xiaonuo.shop.core.excel.converter.ShipperConverter;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/6 17:03
 */
@Data
public class ImportLogisticsDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 659018391439835448L;

    @ExcelProperty("订单号")
    private String orderCode;

    @ExcelProperty(value = "物流公司",converter = ShipperConverter.class)
    private Shipper shipper;

    @ExcelProperty("物流单号")
    private String logisticCode;

    @ExcelProperty("收货人手机号后四位")
    private String customerName;
}
