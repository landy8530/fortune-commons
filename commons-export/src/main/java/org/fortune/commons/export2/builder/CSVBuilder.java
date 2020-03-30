package org.fortune.commons.export2.builder;

import org.fortune.commons.core.util.ApacheBeanUtils;
import org.fortune.commons.core.util.StringUtil;
import org.fortune.commons.export2.domain.ExportCriterion;
import org.fortune.commons.export2.domain.ExportConfig;
import org.fortune.commons.export2.domain.ExportData;
import org.fortune.commons.export2.domain.ExportTableContext;
import org.fortune.commons.export2.element.csv.CSVTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class CSVBuilder extends DocumentBuilder {

    private static final Logger logger = LoggerFactory.getLogger(CSVBuilder.class);

    protected StringBuilder output;

    public CSVBuilder(ExportData<?> exportData, ExportConfig exportConfig) {
        super(exportData, exportConfig);
    }

    @Override
    public CSVBuilder init() {
        logger.info("Initializing the CSV document building...");

        output = new StringBuilder();

        return this;
    }

    @Override
    public CSVBuilder buildTitle() {
        //name
        logger.info("Document Name - " + exportData.getName());

        output.append(StringUtil.escapeCSV(exportData.getName())).append("\n");

        //infos
        if (exportData.getInfos() != null) {
            buildInfos();
        }

        //criteria
        if (exportData.getCriteria() != null) {
            buildCriteria();
        }

        //export time
        logger.info("Export Time - " + getExportTime());

        output.append("Export Time - ").append(getExportTime()).append("\n");

        //blank row
        output.append("\n");

        return this;
    }

    protected void buildCriteria() {
        StringBuilder criteriaBuilder = new StringBuilder();
        criteriaBuilder.append("Criteria - ");

        List<ExportCriterion> criteria = exportConfig.getCriteria();
        for (ExportCriterion exportCriterion : criteria) {
            String criterionValue = ApacheBeanUtils.getStringProperty(exportData.getCriteria(), exportCriterion.getName());

            if (StringUtils.isEmpty(criterionValue)) continue;

            criteriaBuilder.append(exportCriterion.getLabel())
                    .append(": ")
                    .append(criterionValue)
                    .append(" / ");
        }

        if (criteriaBuilder.length() == 11) {
            criteriaBuilder.append("None").append(" / ");
        }

        String criteriaStr = criteriaBuilder.substring(0, criteriaBuilder.length() - 3);
        output.append(StringUtil.escapeCSV(criteriaStr)).append("\n");

        logger.info(criteriaStr);

    }

    @Override
    public CSVBuilder buildSummary() {
        logger.info("Building summary...");

        ExportTableContext summaryContext = new ExportTableContext();
        summaryContext.setExportColumnHeaders(exportConfig.getColSummaryHeaders());
        summaryContext.setColumnData(exportConfig.getColSummaryData());
        summaryContext.setColumnFormats(exportConfig.getColSummaryFormats());
        summaryContext.setColumnWidths(exportConfig.getSummaryWidths());
        summaryContext.addDatum(exportData.getSummary());

        CSVTable summaryTable = new CSVTable(output);
        summaryTable.setContext(summaryContext);

        summaryTable.buildHeader();
        summaryTable.buildBody();

        //blank row
        output.append("\n");

        return this;
    }

    @Override
    public CSVBuilder buildDetail() {
        logger.info("Building detail...");

        ExportTableContext detailContext = new ExportTableContext();
        detailContext.setExportColumnHeaders(exportConfig.getColDetailHeaders());
        detailContext.setColumnData(exportConfig.getColDetailData());
        detailContext.setColumnFormats(exportConfig.getColDetailFormats());
        detailContext.setData((List<Object>) exportData.getData());

        CSVTable detailTable = new CSVTable(output);
        detailTable.setContext(detailContext);

        detailTable.buildHeader();
        detailTable.buildBody();

        return this;
    }

    @Override
    public CSVBuilder finl() {
        logger.info("Finalizing the CSV document building...");
        return this;
    }

    @Override
    public byte[] getDocument() {
        byte[] docBytes = null;

        try {
            docBytes = output.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Error occurs when exporting the CSV document:", e);
        }

        return docBytes;
    }

    protected void buildInfos() {
        for (String key : exportData.getInfos().keySet()) {
            addInfoItem(key, exportData.getInfos().get(key));
        }
    }

    protected void addInfoItem(String label, String value) {
        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append(label + " - " + value);

        String infoStr = infoBuilder.substring(0, infoBuilder.length());
        output.append(StringUtil.escapeCSV(infoStr)).append("\n");
    }

}
