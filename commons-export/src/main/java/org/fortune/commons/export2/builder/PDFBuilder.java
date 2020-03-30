package org.fortune.commons.export2.builder;

import java.io.ByteArrayOutputStream;
import java.util.List;


import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import org.fortune.commons.core.util.ApacheBeanUtils;
import org.fortune.commons.core.util.IOUtils;
import org.fortune.commons.export2.domain.ExportCriterion;
import org.fortune.commons.export2.domain.ExportConfig;
import org.fortune.commons.export2.domain.ExportData;
import org.fortune.commons.export2.domain.ExportTableContext;
import org.fortune.commons.export2.element.pdf.PDFTable;
import org.fortune.commons.export2.element.pdf.Styles;
import org.fortune.commons.export2.element.pdf.factory.CellFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class PDFBuilder extends DocumentBuilder {

    private static final Logger logger = LoggerFactory.getLogger(PDFBuilder.class);

    protected Document document;
    protected ByteArrayOutputStream outputStream;

    protected Styles styles;
    protected CellFactory cellFactory;

    public PDFBuilder(ExportData<?> exportData, ExportConfig exportConfig) {
        super(exportData, exportConfig);
    }

    @Override
    public PDFBuilder init() {
        logger.info("Initializing the PDF document building...");

        outputStream = new ByteArrayOutputStream();
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outputStream));
        document = new Document(pdfDocument, PageSize.A4.rotate());
        document.setMargins(20, 20, 20, 20);

        styles = new Styles(exportConfig.getFontSizes());
        cellFactory = new CellFactory(styles);

        return this;
    }

    @Override
    public PDFBuilder buildTitle() {
        //name
        logger.info("Document Name - " + exportData.getName());

        document.add(new Paragraph(exportData.getName()).addStyle(styles.getTitleStyle()));

        //criteria
        buildCriteria();

        //export time
        logger.info("Export Time - " + getExportTime());

        document.add(
                new Paragraph(new Text("Export Time").addStyle(styles.getBoldStyle()))
                        .add(new Text(" - " + getExportTime()).addStyle(styles.getNormalStyle())));

        //blank row
        document.add(new Paragraph());

        return this;
    }

    protected void buildCriteria() {
        logger.info("Building criteria...");

        Paragraph paragraph = new Paragraph(new Text("Criteria").addStyle(styles.getBoldStyle()))
                .add(new Text(" - ").addStyle(styles.getNormalStyle()));

        int count = 0;
        List<ExportCriterion> criteria = exportConfig.getCriteria();
        for (int i = 0; i < criteria.size(); i++) {
            ExportCriterion exportCriterion = criteria.get(i);
            String criterionValue = ApacheBeanUtils.getStringProperty(exportData.getCriteria(), exportCriterion.getName());

            if (StringUtils.isEmpty(criterionValue)) continue;

            if (count > 0) {
                paragraph.add(new Text(" / ").addStyle(styles.getNormalStyle()));
            }

            paragraph.add(new Text(exportCriterion.getLabel() + ": ").addStyle(styles.getNormalStyle()))
                    .add(new Text(criterionValue).addStyle(styles.getBoldStyle()));

            count++;
        }

        if (count == 0) {
            paragraph.add(new Text("None").addStyle(styles.getNormalStyle()));
        }

        document.add(paragraph);
    }

    @Override
    public PDFBuilder buildSummary() {
        logger.info("Building summary...");

        Table table = new Table(exportConfig.getSummaryColWidthArray());
        table.setWidth(exportConfig.getSummaryWidth());

        ExportTableContext summaryContext = new ExportTableContext();
        summaryContext.setExportColumnHeaders(exportConfig.getColSummaryHeaders());
        summaryContext.setColumnData(exportConfig.getColSummaryData());
        summaryContext.setColumnFormats(exportConfig.getColSummaryFormats());
        summaryContext.setColumnWidths(exportConfig.getSummaryWidths());
        summaryContext.addDatum(exportData.getSummary());

        PDFTable summaryTable = new PDFTable(table);
        summaryTable.setCellFactory(cellFactory);
        summaryTable.setContext(summaryContext);

        summaryTable.buildHeader();
        summaryTable.buildBody();

        document.add(table);

        //blank row
        document.add(new Paragraph());

        return this;
    }

    @Override
    public PDFBuilder buildDetail() {
        logger.info("Building detail...");

        Table table = new Table(exportConfig.getDetailColWidthArray());

        ExportTableContext detailContext = new ExportTableContext();
        detailContext.setExportColumnHeaders(exportConfig.getColDetailHeaders());
        detailContext.setColumnData(exportConfig.getColDetailData());
        detailContext.setColumnFormats(exportConfig.getColDetailFormats());
        detailContext.setColumnWidths(exportConfig.getDetailWidths());
        detailContext.setData((List<Object>) exportData.getData());

        PDFTable detailTable = new PDFTable(table);
        detailTable.setCellFactory(cellFactory);
        detailTable.setContext(detailContext);

        detailTable.buildHeader();
        detailTable.buildBody();

        document.add(table);

        return this;
    }

    @Override
    public PDFBuilder finl() {
        logger.info("Finalizing the PDF document building...");
        return this;
    }

    @Override
    public byte[] getDocument() {
        document.close();

        byte[] docBytes = outputStream.toByteArray();

        IOUtils.closeQuietly(outputStream);

        return docBytes;
    }

}
