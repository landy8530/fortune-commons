package org.fortune.commons.export2.element.excel.factory;

import org.apache.poi.ss.usermodel.*;
import org.fortune.commons.export2.element.excel.Colors;
import org.fortune.commons.export2.element.excel.Fonts;

public class StyleFactory {

    private Workbook workbook;

    private Fonts fonts;

    public StyleFactory(Workbook workbook, Fonts fonts) {
        this.workbook = workbook;
        this.fonts = fonts;
    }

    public CellStyle createTitleStyle() {
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(fonts.getBoldGreenFont());
        titleStyle.setAlignment(HorizontalAlignment.LEFT);

        return titleStyle;
    }

    public CellStyle createSubTitleLabelStyle() {
        CellStyle subTitleLabelStyle = workbook.createCellStyle();
        subTitleLabelStyle.setFont(fonts.getBoldFont());
        subTitleLabelStyle.setAlignment(HorizontalAlignment.RIGHT);

        return subTitleLabelStyle;
    }

    public CellStyle createSubTitleValueStyle() {
        CellStyle subTitleValueStyle = workbook.createCellStyle();
        subTitleValueStyle.setFont(fonts.getBoldFont());
        subTitleValueStyle.setAlignment(HorizontalAlignment.LEFT);

        return subTitleValueStyle;
    }

    public CellStyle createHeaderStyle() {
        CellStyle headerStyle = workbook.createCellStyle();

        headerStyle.setFont(fonts.getBoldWhiteFont());

        setBorderStyle(headerStyle, BorderStyle.THIN);
        setBorderColor(headerStyle, Colors.GREEN);
        headerStyle.setRightBorderColor(Colors.LIGHT_GREEN);
        headerStyle.setLeftBorderColor(Colors.LIGHT_GREEN);
        headerStyle.setFillForegroundColor(Colors.GREEN);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(false);

        return headerStyle;
    }

    public CellStyle createExtHeaderStyle() {
        CellStyle headerStyle = createHeaderStyle();
        headerStyle.setRightBorderColor(Colors.GREEN);

        return headerStyle;
    }

    public CellStyle createDataStyle() {
        CellStyle dataStyle = workbook.createCellStyle();

        dataStyle.setFont(fonts.getNormalFont());

        setBorderStyle(dataStyle, BorderStyle.THIN);
        setBorderColor(dataStyle, Colors.LIGHT_GREY);

        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setWrapText(false);

        return dataStyle;
    }

    private void setBorderStyle(CellStyle cellStyle, BorderStyle borderStyle) {
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setBorderRight(borderStyle);
        cellStyle.setBorderBottom(borderStyle);
        cellStyle.setBorderLeft(borderStyle);
    }

    private void setBorderColor(CellStyle cellStyle, short color) {
        cellStyle.setTopBorderColor(color);
        cellStyle.setRightBorderColor(color);
        cellStyle.setBottomBorderColor(color);
        cellStyle.setLeftBorderColor(color);
    }

}
