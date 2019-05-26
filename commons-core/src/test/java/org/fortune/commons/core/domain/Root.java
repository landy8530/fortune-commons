package org.fortune.commons.core.domain;

import java.sql.Date;
import java.util.List;

public class Root {
    private Long id;
    private List<Child> list;
    private Date date;

    private int index;

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
