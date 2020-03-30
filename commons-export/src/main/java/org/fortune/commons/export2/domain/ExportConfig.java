package org.fortune.commons.export2.domain;

import org.fortune.commons.export2.constant.Constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExportConfig {

    boolean hasExportTime = true;

    private List<ExportCriterion> criteria = new ArrayList<>();
    private List<ExportColumnHeader> colSummaryHeaders = new ArrayList<>();
    private List<ExportColumnDatum> colSummaryData = new ArrayList<>();
    private List<ExportColumnWidth> summaryColWidths = new ArrayList<>();
    private List<ExportColumnHeader> colDetailHeaders = new ArrayList<>();
    private List<ExportColumnDatum> colDetailData = new ArrayList<>();
    private List<ExportColumnWidth> detailColWidths = new ArrayList<>();

    private Map<Integer, String> colSummaryFormats = new LinkedHashMap<>();
    private Map<Integer, String> colDetailFormats = new LinkedHashMap<>();
    private Map<String, Integer> fontSizes = new LinkedHashMap<>();

    public boolean isHasExportTime() {
        return hasExportTime;
    }

    public void setHasExportTime(boolean hasExportTime) {
        this.hasExportTime = hasExportTime;
    }

    public List<ExportCriterion> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<ExportCriterion> criteria) {
        this.criteria = criteria;
    }

    public void setCriterion(String label, String name) {
        criteria.add(new ExportCriterion(label, name));
    }

    public List<ExportColumnHeader> getColSummaryHeaders() {
        return colSummaryHeaders;
    }

    public void setColSummaryHeaders(List<ExportColumnHeader> colSummaryHeaders) {
        this.colSummaryHeaders = colSummaryHeaders;
    }

    public void setColSummaryHeader(Integer index, String header) {
        colSummaryHeaders.add(new ExportColumnHeader(index, header));
    }

    public List<ExportColumnDatum> getColSummaryData() {
        return colSummaryData;
    }

    public void setColSummaryData(List<ExportColumnDatum> colSummaryData) {
        this.colSummaryData = colSummaryData;
    }

    public void setColSummaryDatum(Integer index, String datum) {
        colSummaryData.add(new ExportColumnDatum(index, datum));
    }

    public List<ExportColumnWidth> getSummaryColWidths() {
        return summaryColWidths;
    }

    public void setSummaryColWidths(List<ExportColumnWidth> summaryColWidths) {
        this.summaryColWidths = summaryColWidths;
    }

    public void setSummaryColWidth(Integer index, Integer width) {
        summaryColWidths.add(new ExportColumnWidth(index, width));
    }

    public List<ExportColumnHeader> getColDetailHeaders() {
        return colDetailHeaders;
    }

    public void setColDetailHeaders(List<ExportColumnHeader> colDetailHeaders) {
        this.colDetailHeaders = colDetailHeaders;
    }

    public void setColDetailHeader(Integer index, String header) {
        colDetailHeaders.add(new ExportColumnHeader(index, header));
    }

    public List<ExportColumnDatum> getColDetailData() {
        return colDetailData;
    }

    public void setColDetailData(List<ExportColumnDatum> colDetailData) {
        this.colDetailData = colDetailData;
    }

    public void setColDetailDatum(Integer index, String datum) {
        colDetailData.add(new ExportColumnDatum(index, datum));
    }

    public List<ExportColumnWidth> getDetailColWidths() {
        return detailColWidths;
    }

    public void setDetailColWidths(List<ExportColumnWidth> detailColWidths) {
        this.detailColWidths = detailColWidths;
    }

    public void setDetailColWidth(Integer index, Integer width) {
        detailColWidths.add(new ExportColumnWidth(index, width));
    }

    public Map<Integer, String> getColSummaryFormats() {
        return colSummaryFormats;
    }

    public void setColSummaryFormats(Map<Integer, String> colSummaryFormats) {
        this.colSummaryFormats = colSummaryFormats;
    }

    public void setColSummaryFormat(Integer index, String format) {
        colSummaryFormats.put(index, format);
    }

    public Map<Integer, String> getColDetailFormats() {
        return colDetailFormats;
    }

    public void setColDetailFormats(Map<Integer, String> colDetailFormats) {
        this.colDetailFormats = colDetailFormats;
    }

    public void setColDetailFormat(Integer index, String format) {
        colDetailFormats.put(index, format);
    }

    public float[] getSummaryColWidthArray() {
        return getColumnWidthArray(summaryColWidths);
    }

    public float[] getDetailColWidthArray() {
        return getColumnWidthArray(detailColWidths);
    }

    private float[] getColumnWidthArray(List<ExportColumnWidth> exportColumnWidths) {
        float[] widths = new float[exportColumnWidths.size()];
        for (ExportColumnWidth exportColumnWidth : exportColumnWidths) {
            widths[exportColumnWidth.getIndex()] = exportColumnWidth.getWidth();
        }

        return widths;
    }

    public float getSummaryWidth() {
        float totalSummaryWidth = 0;
        for(float width : getSummaryWidths()) {
            totalSummaryWidth += width;
        }

        return totalSummaryWidth;
    }

    public float[] getSummaryWidths() {
        return getWidths(summaryColWidths);
    }

    public float[] getDetailWidths() {
        return getWidths(detailColWidths);
    }

    private float[] getWidths(List<ExportColumnWidth> exportColumnWidths) {
        int totalWidth = getTotalWidth();
        float[] widths = new float[exportColumnWidths.size()];
        for (ExportColumnWidth exportColumnWidth : exportColumnWidths) {
            widths[exportColumnWidth.getIndex()] = Math.round((exportColumnWidth.getWidth() * 100.00 / totalWidth) * (8.02 - (exportColumnWidths.size()/2) * 0.1));
        }

        return widths;
    }

    private int getTotalWidth() {
        int totalWidth = 0;
        for (ExportColumnWidth exportColumnWidth : detailColWidths) {
            totalWidth += exportColumnWidth.getWidth();
        }

        return totalWidth;
    }

    public Map<String, Integer> getFontSizes() {
        if (fontSizes.get(Constants.FONT_SIZE_LARGE) == null) {
            fontSizes.put(Constants.FONT_SIZE_LARGE, 12);
        }

        if (fontSizes.get(Constants.FONT_SIZE_MIDDLE) == null) {
            fontSizes.put(Constants.FONT_SIZE_MIDDLE, 8);
        }

        if (fontSizes.get(Constants.FONT_SIZE_SMALL) == null) {
            fontSizes.put(Constants.FONT_SIZE_SMALL, 7);
        }

        return fontSizes;
    }

    public void setFontSizes(Map<String, Integer> fontSizes) {
        this.fontSizes = fontSizes;
    }

    public void setFontSize(String name, Integer size) {
        fontSizes.put(name, size);
    }

}
