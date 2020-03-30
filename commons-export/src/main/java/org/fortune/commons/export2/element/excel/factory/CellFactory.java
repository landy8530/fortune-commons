package org.fortune.commons.export2.element.excel.factory;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.fortune.commons.export2.element.excel.Styles;

public class CellFactory {

    private Row row;
    private Styles styles;

    public CellFactory(Row row, Styles styles) {
        this.row = row;
        this.styles = styles;
    }

    public Cell createHeaderCell(int index, String value) {
        Cell cell = row.createCell(index);
        cell.setCellValue(value);
        cell.setCellStyle(styles.getHeaderStyle());

        return cell;
    }

    public Cell createDataCell(int index, Object value, String format) {
        Cell cell = row.createCell(index);
        cell.setCellStyle(styles.getDataStyle(format));

        if (value == null) return cell;

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else {
            cell.setCellValue((String) value);
        }

        return cell;
    }

}
