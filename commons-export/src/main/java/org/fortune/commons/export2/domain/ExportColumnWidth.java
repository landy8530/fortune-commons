package org.fortune.commons.export2.domain;

public class ExportColumnWidth {

    private Integer index;
    private Integer width;

    public ExportColumnWidth(Integer index, Integer width) {
        this.index = index;
        this.width = width;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

}
