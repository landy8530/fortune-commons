package org.fortune.commons.export2.element.pdf.factory;

import java.util.Date;


import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.fortune.commons.core.util.DateUtil;
import org.fortune.commons.core.util.NumberUtil;
import org.fortune.commons.export2.constant.CellDataFormat;
import org.fortune.commons.export2.element.pdf.Styles;

public class CellFactory {

    private Styles styles;

    public CellFactory(Styles styles) {
        this.styles = styles;
    }

    public Cell createHeaderCell(String value, float width) {
        Cell cell = new Cell()
                .add(new Paragraph(value))
                .addStyle(styles.getHeaderStyle())
                .setWidth(width);
        return cell;
    }

    public Cell createDataCell(Object value, String format, float width) {
        Cell cell = getCell(value, format, width)
                .addStyle(styles.getDataStyle());

        return cell;
    }

    private Cell getCell(Object value, String format, float width) {
        Cell cell = new Cell();

        if (value == null) return cell;

        Paragraph paragraph = new Paragraph()
                    .setWidth(width);

        switch (format) {
            case CellDataFormat.NUMBER:
                cell.add(paragraph.add(NumberUtil.getNumber(value)));
                cell.setTextAlignment(TextAlignment.RIGHT);
                break;

            case CellDataFormat.AMOUNT:
                cell.add(paragraph.add(NumberUtil.getCurrency(value)));
                cell.setTextAlignment(TextAlignment.RIGHT);
                break;

            case CellDataFormat.DATE:
                cell.add(paragraph.add(DateUtil.date2String((Date) value, DateUtil.PATTERN_SHORT_DATE_SLASH)));
                cell.setTextAlignment(TextAlignment.RIGHT);
                break;

            case CellDataFormat.DATE_TIME:
                cell.add(paragraph.add(DateUtil.date2String((Date) value, DateUtil.PATTERN_FULL_DATE_TIME_24)));
                cell.setTextAlignment(TextAlignment.RIGHT);
                break;

            case CellDataFormat.PERCENTAGE:
                cell.add(paragraph.add(NumberUtil.getPercentage(value)));
                cell.setTextAlignment(TextAlignment.RIGHT);
                break;

            default:
                cell.add(paragraph.add(String.valueOf(value)));
                if (value instanceof Integer || value instanceof Long) {
                    cell.setTextAlignment(TextAlignment.RIGHT);
                }
        }

        return cell;
    }

}
