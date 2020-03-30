package org.fortune.commons.export2.builder;


import org.fortune.commons.core.util.DateUtil;
import org.fortune.commons.export2.domain.ExportConfig;
import org.fortune.commons.export2.domain.ExportData;

public abstract class DocumentBuilder {

    protected ExportData<?> exportData;
    protected ExportConfig exportConfig;

    public DocumentBuilder() {}

    public DocumentBuilder(ExportData<?> exportData, ExportConfig exportConfig) {
        this.exportData = exportData;
        this.exportConfig = exportConfig;
    }

    public abstract DocumentBuilder init();

    public abstract DocumentBuilder buildTitle();

    public abstract DocumentBuilder buildSummary();

    public abstract DocumentBuilder buildDetail();

    public abstract DocumentBuilder finl();

    public abstract byte[] getDocument();

    protected String getExportTime() {
        return DateUtil.getCurrentDateTime();
    }

}
