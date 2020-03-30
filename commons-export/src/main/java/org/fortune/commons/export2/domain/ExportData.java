package org.fortune.commons.export2.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportData<T> {

    private String name;

    private String sheetName;

    private Object criteria;

    private Map<String, String> infos;

    private Map<String, Object> summary;

    private List<T> data;

    public ExportData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Object getCriteria() {
        return criteria;
    }

    public void setCriteria(Object criteria) {
        this.criteria = criteria;
    }

    public Map<String, String> getInfos() {
        return infos;
    }

    public void setInfos(Map<String, String> infos) {
        this.infos = infos;
    }

    public Map<String, Object> getSummary() {
        return summary;
    }

    public void setSummary(Map<String, Object> summary) {
        this.summary = summary;
    }

    public void addSummaryEntry(String key, Object value) {
        if (summary == null) {
            summary = new HashMap<>();
        }

        summary.put(key, value);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
