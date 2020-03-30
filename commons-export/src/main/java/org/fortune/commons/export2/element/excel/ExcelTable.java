package org.fortune.commons.export2.element.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.fortune.commons.core.util.ApacheBeanUtils;
import org.fortune.commons.export2.domain.ExportColumnDatum;
import org.fortune.commons.export2.domain.ExportColumnHeader;
import org.fortune.commons.export2.domain.ExportTableContext;
import org.fortune.commons.export2.element.excel.factory.CellFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelTable {

    private static final Logger logger = LoggerFactory.getLogger(ExcelTable.class);

    private int startRow;

    private Sheet sheet;
    private Styles styles;

    private ExportTableContext context;

    public ExcelTable(Sheet sheet, int startRow) {
        this.sheet = sheet;
        this.startRow = startRow;
    }

    public void buildHeader() {
        List<ExportColumnHeader> exportColumnHeaders = context.getExportColumnHeaders();

        logger.info("Building the table header: " + exportColumnHeaders.size() + " columns");

        Row row = sheet.createRow(startRow++);
        for (int i = 0; i < exportColumnHeaders.size(); i++) {
            ExportColumnHeader exportColumnHeader = exportColumnHeaders.get(i);

            logger.info("Column " + (i + 1) + ": " + exportColumnHeader.getHeader());

            CellFactory cellFactory = new CellFactory(row, styles);
            Cell cell = cellFactory.createHeaderCell(exportColumnHeader.getIndex(), exportColumnHeader.getHeader());

            if (i == exportColumnHeaders.size() - 1) {
                cell.setCellStyle(styles.getExtHeaderStyle());
            }
        }
    }

    public void buildBody() {
        List<ExportColumnDatum> columnData = context.getColumnData();
        List<Object> data = context.getData();
        Map<Integer, String> columnFormats = context.getColumnFormats();

        logger.info("Building the table body: " + columnData.size() + " columns, " + data.size() + " rows");

        for (int i = 0; i < data.size(); i++) {
            //logger.info("Row " + (i + 1));

            Row row = sheet.createRow(startRow++);
            for (int j = 0; j < columnData.size(); j++) {
                ExportColumnDatum exportColumnDatum = columnData.get(j);

                //logger.info("Column " + (j + 1) + ": " + BeanUtil.getStringProperty(data.get(i), exportColumnDatum.getDatum()));

                CellFactory cellFactory = new CellFactory(row, styles);
                cellFactory.createDataCell(exportColumnDatum.getIndex(),
                        ApacheBeanUtils.getProperty(data.get(i), exportColumnDatum.getDatum()),
                        columnFormats.get(exportColumnDatum.getIndex()));
            }
        }
    }

    public void setStyles(Styles styles) {
        this.styles = styles;
    }

    public void setContext(ExportTableContext context) {
        this.context = context;
    }

}
