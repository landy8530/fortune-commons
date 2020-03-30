package org.fortune.commons.export2.element.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;
import org.fortune.commons.export2.constant.CellDataFormat;
import org.fortune.commons.export2.element.excel.factory.StyleFactory;

public class Styles {

    private CellStyle titleStyle;
    private CellStyle subTitleLabelStyle;
    private CellStyle subTitleValueStyle;
    private CellStyle headerStyle;
    private CellStyle extHeaderStyle;
    private CellStyle generalDataStyle;
    private CellStyle textDataStyle;
    private CellStyle numberDataStyle;
    private CellStyle amountDataStyle;
    private CellStyle dateDataStyle;
    private CellStyle dateTimeDataStyle;
    private CellStyle percentageStyle;

    private StyleFactory styleFactory;

    private DataFormat dataFormat;

    public Styles(Workbook workbook, Fonts fonts) {
        styleFactory = new StyleFactory(workbook, fonts);
        titleStyle = styleFactory.createTitleStyle();
        subTitleLabelStyle = styleFactory.createSubTitleLabelStyle();
        subTitleValueStyle = styleFactory.createSubTitleValueStyle();
        headerStyle = styleFactory.createHeaderStyle();
        extHeaderStyle = styleFactory.createExtHeaderStyle();

        this.dataFormat = workbook.getCreationHelper().createDataFormat();
    }

    public CellStyle getTitleStyle() {
        return titleStyle;
    }

    public CellStyle getSubTitleLabelStyle() {
        return subTitleLabelStyle;
    }

    public CellStyle getSubTitleValueStyle() {
        return subTitleValueStyle;
    }

    public CellStyle getHeaderStyle() {
        return headerStyle;
    }

    public CellStyle getExtHeaderStyle() {
        return extHeaderStyle;
    }

    public CellStyle getDataStyle(String format) {
        switch (format) {
            case CellDataFormat.TEXT:
                return getTextDataStyle();

            case CellDataFormat.NUMBER:
                return getNumberDataStyle();

            case CellDataFormat.DOLLAR_AMOUNT:
                return getDollarAmountDataStyle();

            case CellDataFormat.AMOUNT:
                return getAmountDataStyle();

            case CellDataFormat.DATE:
                return getDateDataStyle();

            case CellDataFormat.DATE_TIME:
                return getDateTimeDataStyle();

            case CellDataFormat.PERCENTAGE:
                return getPercentageStyle();

            default:
                return getGeneralDataStyle();
        }
    }

    public CellStyle getGeneralDataStyle() {
        if (generalDataStyle == null) {
            generalDataStyle = styleFactory.createDataStyle();
            generalDataStyle.setDataFormat(dataFormat.getFormat("General"));
        }

        return generalDataStyle;
    }

    public CellStyle getTextDataStyle() {
        if (textDataStyle == null) {
            textDataStyle = styleFactory.createDataStyle();
            textDataStyle.setDataFormat(dataFormat.getFormat("@"));
        }

        return textDataStyle;
    }

    public CellStyle getNumberDataStyle() {
        if (numberDataStyle == null) {
            numberDataStyle = styleFactory.createDataStyle();
            numberDataStyle.setDataFormat(dataFormat.getFormat("#,##0"));
        }

        return numberDataStyle;
    }

    public CellStyle getDollarAmountDataStyle() {
        if (amountDataStyle == null) {
            amountDataStyle = styleFactory.createDataStyle();
            amountDataStyle.setDataFormat(dataFormat.getFormat("$#,##0.00"));
        }

        return amountDataStyle;
    }

    public CellStyle getAmountDataStyle() {
        if (amountDataStyle == null) {
            amountDataStyle = styleFactory.createDataStyle();
            amountDataStyle.setDataFormat(dataFormat.getFormat("￥#,##0.00"));
        }

        return amountDataStyle;
    }

    public CellStyle getDateDataStyle() {
        if (dateDataStyle == null) {
            dateDataStyle = styleFactory.createDataStyle();
            dateDataStyle.setDataFormat(dataFormat.getFormat("m/d/yyyy"));
        }

        return dateDataStyle;
    }

    public CellStyle getDateTimeDataStyle() {
        if (dateTimeDataStyle == null) {
            dateTimeDataStyle = styleFactory.createDataStyle();
            dateTimeDataStyle.setDataFormat(dataFormat.getFormat("mm/dd/yyyy hh:mm:ss"));
        }

        return dateTimeDataStyle;
    }

    public CellStyle getPercentageStyle() {
        if (percentageStyle == null) {
            percentageStyle = styleFactory.createDataStyle();
            percentageStyle.setDataFormat(dataFormat.getFormat("###%"));
        }

        return percentageStyle;
    }

}
