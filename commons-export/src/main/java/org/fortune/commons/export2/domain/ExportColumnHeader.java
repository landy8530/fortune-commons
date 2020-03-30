package org.fortune.commons.export2.domain;

public class ExportColumnHeader {

    private Integer index;
    private String header;

    public ExportColumnHeader(Integer index, String header) {
        this.index = index;
        this.header = header;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

}
