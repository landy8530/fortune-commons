package org.fortune.commons.export2.element.pdf;

import java.util.List;
import java.util.Map;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import org.fortune.commons.core.util.ApacheBeanUtils;
import org.fortune.commons.export2.domain.ExportColumnDatum;
import org.fortune.commons.export2.domain.ExportColumnHeader;
import org.fortune.commons.export2.domain.ExportTableContext;
import org.fortune.commons.export2.element.pdf.factory.CellFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PDFTable {

    private static final Logger logger = LoggerFactory.getLogger(PDFTable.class);

    private Table table;
    private CellFactory cellFactory;

    private ExportTableContext context;

    public PDFTable(Table table) {
        this.table = table;
    }

    public void buildHeader() {
        List<ExportColumnHeader> exportColumnHeaders = context.getExportColumnHeaders();
        float[] columnWidths = context.getColumnWidths();

        logger.info("Building the table header: " + exportColumnHeaders.size() + " columns");

        for (int i = 0; i < exportColumnHeaders.size(); i++) {
            ExportColumnHeader exportColumnHeader = exportColumnHeaders.get(i);

            logger.info("Column " + (i + 1) + ": " + exportColumnHeader.getHeader());

            Cell cell = cellFactory.createHeaderCell(
                    exportColumnHeader.getHeader(),
                    columnWidths[exportColumnHeader.getIndex()]);

            if (i < exportColumnHeaders.size() - 1) {
                cell.setBorderRight(Borders.SOLID_LIGHT_GREEN);
            }

            table.addHeaderCell(cell);
        }
    }

    public void buildBody() {
        List<ExportColumnDatum> columnData = context.getColumnData();
        List<Object> data = context.getData();
        Map<Integer, String> columnFormats = context.getColumnFormats();
        float[] columnWidths = context.getColumnWidths();

        logger.info("Building the table body: " + columnData.size() + " columns, " + data.size() + " rows");

        for (int i = 0; i < data.size(); i++) {
            //logger.info("Row " + (i + 1));

            for (int j = 0; j < columnData.size(); j++) {
                ExportColumnDatum exportColumnDatum = columnData.get(j);

                //logger.info("Column " + (j + 1) + ": " + BeanUtil.getStringProperty(data.get(i), exportColumnDatum.getDatum()));

                Cell cell = cellFactory.createDataCell(
                        ApacheBeanUtils.getProperty(data.get(i), exportColumnDatum.getDatum()),
                        columnFormats.get(exportColumnDatum.getIndex()), columnWidths[exportColumnDatum.getIndex()]);
                table.addCell(cell);
            }
        }
    }

    public void setCellFactory(CellFactory cellFactory) {
        this.cellFactory = cellFactory;
    }

    public void setContext(ExportTableContext context) {
        this.context = context;
    }

}
