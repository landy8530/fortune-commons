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

    /**
     * 默认用ClassLoad 的方式加载模版
     */
    private TemplateLoadingType templateLoadingType = TemplateLoadingType.CLASS;

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

    public TemplateLoadingType getTemplateLoadingType() {
        return templateLoadingType;
    }

    public void setTemplateLoadingType(TemplateLoadingType templateLoadingType) {
        this.templateLoadingType = templateLoadingType;
    }

    public enum TemplateLoadingType {
        FILE,
        CLASS
    }
}
