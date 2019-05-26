package org.fortune.commons.core.domain;

import java.sql.Date;
import java.util.List;

public class RootCopier {

    private Long id;
    private List<Child> list;
    private Date date;

    private Integer index;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Child> getList() {
        return list;
    }

    public void setList(List<Child> list) {
        this.list = list;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
