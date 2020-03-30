package org.fortune.commons.export2.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExportTableContext {

    private float[] columnWidths;

    private Map<Integer, String> columnFormats;

    private List<ExportColumnHeader> exportColumnHeaders;
    private List<ExportColumnDatum> columnData;
    private List<Object> data;

    public float[] getColumnWidths() {
        return columnWidths;
    }

    public void setColumnWidths(float[] columnWidths) {
        this.columnWidths = columnWidths;
    }

    public Map<Integer, String> getColumnFormats() {
        return columnFormats;
    }

    public void setColumnFormats(Map<Integer, String> columnFormats) {
        this.columnFormats = columnFormats;
    }

    public List<ExportColumnHeader> getExportColumnHeaders() {
        return exportColumnHeaders;
    }

    public void setExportColumnHeaders(List<ExportColumnHeader> exportColumnHeaders) {
        this.exportColumnHeaders = exportColumnHeaders;
    }

    public List<ExportColumnDatum> getColumnData() {
        return columnData;
    }

    public void setColumnData(List<ExportColumnDatum> columnData) {
        this.columnData = columnData;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public void addDatum(Object datum) {
        if (data == null) {
            data = new ArrayList<>();
        }

        data.add(datum);
    }

}
