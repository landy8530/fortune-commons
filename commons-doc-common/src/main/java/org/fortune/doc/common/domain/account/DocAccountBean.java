package org.fortune.doc.common.domain.account;

import java.util.List;

/**
 * @author: landy
 * @date: 2019/6/16 11:10
 * @description:
 */
public class DocAccountBean extends Account {

    private List<ImageDocThumbBean> thumbConfig;
    private String watermark;

    public List<ImageDocThumbBean> getThumbConfig() {
        return thumbConfig;
    }

    public void setThumbConfig(List<ImageDocThumbBean> thumbConfig) {
        this.thumbConfig = thumbConfig;
    }

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }
}
