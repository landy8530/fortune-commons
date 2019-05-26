package org.fortune.commons.core.freemarker;

import java.util.Map;

/**
 * @author: Landy
 * @date: 2019/4/6 19:46
 * @description:
 */
public class FtlResourceData {

    /**
     * Ftl模板路径
     */
    private  String ftlTemplatePath;

    /**
     * 数据
     */
    private Map<String,Object> data;

    public String getFtlTemplatePath() {
        return ftlTemplatePath;
    }

    public void setFtlTemplatePath(String ftlTemplatePath) {
        this.ftlTemplatePath = ftlTemplatePath;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

}
