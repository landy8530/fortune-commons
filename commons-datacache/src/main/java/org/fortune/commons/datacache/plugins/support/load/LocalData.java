package org.fortune.commons.datacache.plugins.support.load;

public class LocalData {

    private Object data;
    private long time;

    public LocalData() {
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
