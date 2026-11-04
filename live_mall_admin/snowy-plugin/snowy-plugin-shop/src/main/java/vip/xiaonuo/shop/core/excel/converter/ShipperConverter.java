package vip.xiaonuo.shop.core.excel.converter;

import api.logistics.common.enums.Shipper;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/6 17:13
 */
public class ShipperConverter implements Converter<Shipper> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Shipper.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    // 从 Excel 字符串转成枚举
    @Override
    public Shipper convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return Shipper.getByName(cellData.getStringValue());
    }

    @Override
    public WriteCellData<?> convertToExcelData(Shipper value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(value.getName());
    }
}
