package org.fortune.commons.export2.domain;

public class ExportColumnDatum {

    private Integer index;
    private String datum;

    public ExportColumnDatum(Integer index, String datum) {
        this.index = index;
        this.datum = datum;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

}
