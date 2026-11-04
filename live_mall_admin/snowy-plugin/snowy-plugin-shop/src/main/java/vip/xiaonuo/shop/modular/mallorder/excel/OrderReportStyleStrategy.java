package vip.xiaonuo.shop.modular.mallorder.excel;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.jetbrains.annotations.NotNull;

/**
 * <p>自定义excel格式</p>
 *
 * @author goya
 * @since 2025/5/7 14:19
 */
@UtilityClass
public class OrderReportStyleStrategy {

    public static HorizontalCellStyleStrategy getStrategy() {
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        WriteFont headFont = new WriteFont();
        headFont.setBold(true);
        headFont.setFontHeightInPoints((short) 12);
        headStyle.setWriteFont(headFont);

        WriteCellStyle contentStyle = getWriteCellStyle();

        return new HorizontalCellStyleStrategy(headStyle, contentStyle);
    }

    @NotNull
    private static WriteCellStyle getWriteCellStyle() {
        WriteCellStyle contentStyle = new WriteCellStyle();
        contentStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentStyle.setBorderBottom(BorderStyle.THIN);
        contentStyle.setBorderTop(BorderStyle.THIN);
        contentStyle.setBorderLeft(BorderStyle.THIN);
        contentStyle.setBorderRight(BorderStyle.THIN);

        WriteFont contentFont = new WriteFont();
        contentFont.setFontHeightInPoints((short) 11);
        contentStyle.setWriteFont(contentFont);
        return contentStyle;
    }
}
