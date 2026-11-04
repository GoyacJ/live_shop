package vip.xiaonuo.shop.modular.mallorder.excel;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;

/**
 * <p></p>
 *
 * @author goya
 * @since 2025/5/7 13:08
 */
public class OrderSummaryWriteHandler implements SheetWriteHandler {

    private final String gmv;
    private final String salesAmount;
    private final String onlineActualReceipt;

    public OrderSummaryWriteHandler(String gmv, String salesAmount, String onlineActualReceipt) {
        this.gmv = gmv;
        this.salesAmount = salesAmount;
        this.onlineActualReceipt = onlineActualReceipt;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = writeWorkbookHolder.getWorkbook();

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        // 第1行：GMV
        Row row1 = sheet.createRow(0);
        Cell cell1 = row1.createCell(0);
        cell1.setCellValue("GMV：" + gmv);
        cell1.setCellStyle(style);

        // 第2行：销售金额
        Row row2 = sheet.createRow(1);
        Cell cell2 = row2.createCell(0);
        cell2.setCellValue("销售金额(元)：" + salesAmount);
        cell2.setCellStyle(style);

        // 第3行：线上实收
        Row row3 = sheet.createRow(2);
        Cell cell3 = row3.createCell(0);
        cell3.setCellValue("线上实收(元)：" + onlineActualReceipt);
        cell3.setCellStyle(style);
    }
}
