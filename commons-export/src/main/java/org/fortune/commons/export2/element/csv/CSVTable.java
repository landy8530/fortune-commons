package org.fortune.commons.export2.element.csv;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.fortune.commons.core.util.ApacheBeanUtils;
import org.fortune.commons.core.util.DateUtil;
import org.fortune.commons.core.util.StringUtil;
import org.fortune.commons.export2.constant.CellDataFormat;
import org.fortune.commons.export2.domain.ExportColumnDatum;
import org.fortune.commons.export2.domain.ExportColumnHeader;
import org.fortune.commons.export2.domain.ExportTableContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVTable {

    private static final Logger logger = LoggerFactory.getLogger(CSVTable.class);

    private StringBuilder output;

    private ExportTableContext context;

    public CSVTable(StringBuilder output) {
        this.output = output;
    }

    public void buildHeader() {
        List<ExportColumnHeader> exportColumnHeaders = context.getExportColumnHeaders();

        logger.info("Building the table header: " + exportColumnHeaders.size() + " columns");

        StringBuilder headerBuilder = new StringBuilder();
        for (int i = 0; i < exportColumnHeaders.size(); i++) {
            ExportColumnHeader exportColumnHeader = exportColumnHeaders.get(i);

            logger.info("Column " + (i + 1) + ": " + exportColumnHeader.getHeader());

            headerBuilder.append(StringUtil.escapeCSV(StringUtil.null2String(exportColumnHeader.getHeader())))
                    .append(",");
        }

        output.append(headerBuilder.length() == 0 ? "" : headerBuilder.substring(0, headerBuilder.length() - 1))
                .append("\n");
    }

    public void buildBody() {
        List<ExportColumnDatum> columnData = context.getColumnData();
        List<Object> data = context.getData();
        Map<Integer, String> columnFormats = context.getColumnFormats();

        logger.info("Building the table body: " + columnData.size() + " columns, " + data.size() + " rows");

        for (int i = 0; i < data.size(); i++) {
            //logger.info("Row " + (i + 1));

            StringBuilder dataBuilder = new StringBuilder();
            for (int j = 0; j < columnData.size(); j++) {
                ExportColumnDatum exportColumnDatum = columnData.get(j);

                //logger.info("Column " + (j + 1) + ": " + BeanUtil.getStringProperty(data.get(i), exportColumnDatum.getDatum()));

                dataBuilder.append(
                        StringUtil.escapeCSV(formatValue(
                                ApacheBeanUtils.getProperty(data.get(i), exportColumnDatum.getDatum()),
                                columnFormats.get(exportColumnDatum.getIndex()))))
                        .append(",");
            }

            output.append(dataBuilder.length() == 0 ? "" : dataBuilder.substring(0, dataBuilder.length() - 1))
                    .append("\n");
        }
    }

    private String formatValue(Object value, String format) {
        if (null == value) return "";

        switch (format) {
            case CellDataFormat.DATE:
                return DateUtil.date2String((Date) value, DateUtil.PATTERN_SHORT_DATE_SLASH);

            case CellDataFormat.DATE_TIME:
                return DateUtil.date2String((Date) value, DateUtil.PATTERN_FULL_DATE_TIME_24);

            default:
                return String.valueOf(value);
        }
    }

    public void setContext(ExportTableContext context) {
        this.context = context;
    }

}
