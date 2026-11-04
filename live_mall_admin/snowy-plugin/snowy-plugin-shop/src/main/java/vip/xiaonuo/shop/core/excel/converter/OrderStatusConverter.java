package vip.xiaonuo.shop.core.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import vip.xiaonuo.common.enums.OrderStatusEnum;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/7 15:51
 */
public class OrderStatusConverter implements Converter<OrderStatusEnum> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return OrderStatusEnum.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public WriteCellData<?> convertToExcelData(OrderStatusEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(value.getDescription());
    }
}
