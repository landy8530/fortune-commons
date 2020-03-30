package org.fortune.commons.export2.builder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fortune.commons.core.util.ApacheBeanUtils;
import org.fortune.commons.core.util.IOUtils;
import org.fortune.commons.export2.domain.ExportCriterion;
import org.fortune.commons.export2.domain.ExportConfig;
import org.fortune.commons.export2.domain.ExportData;
import org.fortune.commons.export2.domain.ExportTableContext;
import org.fortune.commons.export2.element.excel.ExcelTable;
import org.fortune.commons.export2.element.excel.Fonts;
import org.fortune.commons.export2.element.excel.Styles;
import org.fortune.commons.export2.element.excel.factory.SheetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class ExcelBuilder extends DocumentBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ExcelBuilder.class);

    private List<ExportData<?>> exportDatas = new ArrayList<>();
    private List<ExportConfig> exportConfigs = new ArrayList<>();



    private List<Sheet> sheets = new ArrayList<>();
    private List<Integer> rowNumbers = new ArrayList<>();
    private List<Integer> headerRowNumbers = new ArrayList<>();

    private Integer rowNumber;
    private int sheetAmount;

    private Workbook workbook;
    private Sheet sheet;
    private Row row;
    private Cell cell;
    private Fonts fonts;
    private Styles styles;

    public ExcelBuilder(ExportData<?> exportData, ExportConfig exportConfig) {
        exportDatas.add(exportData);
        exportConfigs.add(exportConfig);
    }

    public ExcelBuilder(List<ExportData<?>> exportDatas, List<ExportConfig> exportConfigs) {
        this.exportDatas = exportDatas;
        this.exportConfigs = exportConfigs;
    }

    @Override
    public ExcelBuilder init() {
        logger.info("Initializing the excel document building...");

        workbook = new XSSFWorkbook();
        fonts = new Fonts(workbook);
        styles = new Styles(workbook, fonts);
        sheetAmount = exportDatas.size();

        for (int i = 0; i < sheetAmount; i++) {
            exportData = exportDatas.get(i);
            exportConfig = exportConfigs.get(i);

            SheetFactory sheetFactory = new SheetFactory(workbook);
            String sheetName = StringUtils.isEmpty(exportData.getSheetName()) ? exportData.getName() : exportData.getSheetName();
            sheet = sheetFactory.createSheet(sheetName, exportConfig.getDetailColWidths());

            rowNumber = 0;

            sheets.add(sheet);
            rowNumbers.add(rowNumber);
            headerRowNumbers.add(0);
        }

        return this;
    }

    private void getParams(int i) {
        exportData = exportDatas.get(i);
        exportConfig = exportConfigs.get(i);
        sheet = sheets.get(i);
        rowNumber = rowNumbers.get(i);
    }

    private void setParams(int i) {
        sheets.set(i, sheet);
        rowNumbers.set(i, rowNumber);
    }

    @Override
    public ExcelBuilder buildTitle() {
        for (int i = 0; i < sheetAmount; i++) {
            getParams(i);

            //name
            row = sheet.createRow(rowNumber++);
            cell = row.createCell(0);
            cell.setCellValue(exportData.getName());
            cell.setCellStyle(styles.getTitleStyle());

            logger.info("Document Name - " + exportData.getName());

            //infos
            if (exportData.getInfos() != null) {
                buildInfos();
            }

            //criteria
            if (exportData.getCriteria() != null) {
                buildCriteria();
            }

            //export Time
            if (exportConfig.isHasExportTime()) {
                buildExportTime();
            }

            //blank row
            rowNumber++;

            setParams(i);
        }

        return this;
    }

    protected ExcelBuilder buildExportTime() {
        String label = "Export Time";
        addInfoItem(label, getExportTime(), null);

        return this;
    }

    protected ExcelBuilder buildInfos() {
        for (String key : exportData.getInfos().keySet()) {
            addInfoItem(key, exportData.getInfos().get(key), null);
        }

        return this;
    }

    protected void buildCriteria() {
        String label = "Criteria";

        int startIndex = 0;
        StringBuilder criteriaBuilder = new StringBuilder();
        Map<Integer, Integer> boldRanges = new LinkedHashMap<>();

        List<ExportCriterion> criteria = exportConfig.getCriteria();
        for (ExportCriterion exportCriterion : criteria) {
            String criterionValue = ApacheBeanUtils.getStringProperty(exportData.getCriteria(), exportCriterion.getName());

            if (StringUtils.isEmpty(criterionValue)) {
                continue;
            }

            criteriaBuilder.append(exportCriterion.getLabel() + " - ");
            startIndex = criteriaBuilder.length();
            criteriaBuilder.append(criterionValue);
            boldRanges.put(startIndex, criteriaBuilder.length());
            criteriaBuilder.append(" / ");
        }

        if (criteriaBuilder.length() == 0) {
            criteriaBuilder.append("None");
            criteriaBuilder.append(" / ");
        }

        addInfoItem(label, criteriaBuilder.substring(0, criteriaBuilder.length() - 3), boldRanges);
    }

    protected void addInfoItem(String label, String value, Map<Integer, Integer> boldRanges) {
        row = sheet.createRow(rowNumber++);
        cell = row.createCell(0);
        RichTextString rtsLabel = new XSSFRichTextString(label + ":");
        rtsLabel.applyFont(fonts.getNormalFont());
        rtsLabel.applyFont(0, label.length() + 1, fonts.getBoldFont());
        cell.setCellValue(rtsLabel);
        cell.setCellStyle(styles.getSubTitleLabelStyle());

        cell = row.createCell(1);
        RichTextString rtsValue = new XSSFRichTextString(value);
        rtsValue.applyFont(fonts.getNormalFont());
        if (null != boldRanges) {
            for (Map.Entry<Integer, Integer> rangeEntry : boldRanges.entrySet()) {
                rtsValue.applyFont(rangeEntry.getKey(), rangeEntry.getValue(), fonts.getBoldFont());
            }
        }
        cell.setCellValue(rtsValue);
        cell.setCellStyle(styles.getSubTitleValueStyle());

        logger.info(rtsLabel.getString() + rtsValue.getString());
    }

    @Override
    public ExcelBuilder buildSummary() {
        for (int i = 0; i < sheetAmount; i++) {
            getParams(i);

            logger.info("Building " + sheet.getSheetName() + " summary...");

            ExportTableContext summaryContext = new ExportTableContext();
            summaryContext.setExportColumnHeaders(exportConfig.getColSummaryHeaders());
            summaryContext.setColumnData(exportConfig.getColSummaryData());
            summaryContext.setColumnFormats(exportConfig.getColSummaryFormats());
            summaryContext.addDatum(exportData.getSummary());

            ExcelTable summaryTable = new ExcelTable(sheet, rowNumber);
            summaryTable.setStyles(styles);
            summaryTable.setContext(summaryContext);

            summaryTable.buildHeader();
            summaryTable.buildBody();

            rowNumber += 2;

            //space
            rowNumber++;

            setParams(i);
        }

        return this;
    }

    @Override
    public ExcelBuilder buildDetail() {
        for (int i = 0; i < sheetAmount; i++) {
            getParams(i);

            logger.info("Building " + sheet.getSheetName() + " detail...");

            headerRowNumbers.set(i, rowNumber + 1);

            ExportTableContext detailContext = new ExportTableContext();
            detailContext.setExportColumnHeaders(exportConfig.getColDetailHeaders());
            detailContext.setColumnData(exportConfig.getColDetailData());
            detailContext.setColumnFormats(exportConfig.getColDetailFormats());
            detailContext.setData((List<Object>) exportData.getData());

            ExcelTable detailTable = new ExcelTable(sheet, rowNumber);
            detailTable.setStyles(styles);
            detailTable.setContext(detailContext);

            detailTable.buildHeader();
            detailTable.buildBody();

            setParams(i);
        }

        return this;
    }

    @Override
    public ExcelBuilder finl() {
        logger.info("Finalizing the excel document building...");

        for (int i = 0; i < sheetAmount; i++) {
            getParams(i);
            sheet.createFreezePane(0, headerRowNumbers.get(i));
        }

        return this;
    }

    @Override
    public byte[] getDocument() {
        byte[] docBytes = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            workbook.write(outputStream);
            docBytes = outputStream.toByteArray();
        } catch (IOException e) {
            logger.error("Error occurs when exporting the excel document:", e);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                logger.error("Error occurs when closing the excel workbook:", e);
            }

            IOUtils.closeQuietly(outputStream);
        }

        return docBytes;
    }

}
