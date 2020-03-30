package org.fortune.commons.export2.builder.factory;

import org.fortune.commons.export2.builder.CSVBuilder;
import org.fortune.commons.export2.builder.DocumentBuilder;
import org.fortune.commons.export2.builder.ExcelBuilder;
import org.fortune.commons.export2.builder.PDFBuilder;
import org.fortune.commons.export2.constant.Constants;
import org.fortune.commons.export2.domain.ExportConfig;
import org.fortune.commons.export2.domain.ExportData;

import java.util.List;

public class DocumentBuilderFactory {

    public DocumentBuilder createDocumentBuilder(String documentType, ExportData<?> exportData, ExportConfig exportConfig) {
        switch (documentType) {
            case Constants.DOCUMENT_TYPE_EXCEL:
                return new ExcelBuilder(exportData, exportConfig);

            case Constants.DOCUMENT_TYPE_PDF:
                return new PDFBuilder(exportData, exportConfig);

            case Constants.DOCUMENT_TYPE_CSV:
            default:
                return new CSVBuilder(exportData, exportConfig);
        }
    }

    public DocumentBuilder createDocumentBuilder(List<ExportData<?>> exportDatas, List<ExportConfig> exportConfigs) {
        return new ExcelBuilder(exportDatas, exportConfigs);
    }

}
