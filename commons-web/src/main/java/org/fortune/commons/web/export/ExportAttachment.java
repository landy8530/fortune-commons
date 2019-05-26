package org.fortune.commons.web.export;

import javax.servlet.http.HttpServletRequest;


/**
 * @author: Landy
 * @date: 2019/4/15 23:15
 * @version: 1.0
 */
public class ExportAttachment {


    public static ExportAttachment NULL_ATTACH_INFO = new ExportAttachment(null);

    public enum ContentDisplayType {
        /**
         * 附件弹出另存方式框
         */
        attachment("附件弹出另存方式框", "attachment"),

        /**
         * 附件在线直接打开
         */
        inline("附件在线直接打开", "inline");

        String name;
        String value;

        ContentDisplayType(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }


    public static String EXPORT_ATTACH_TO_REQUEST_KEY = "fortune_export_attach";

    /**
     * 导出PDF
     */
    public static String EXPORT_PDF_SUFFIX = ".pdf";
    /**
     * 导出xls
     */
    public static String EXPORT_XLS_SUFFIX = ".xls";

    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 导出文件显示名称
     */
    private String displayFileName;
    /**
     * 内容呈现方式，见枚举：ContentDisplayType
     */
    private String contentDisplayType;
    /**
     * 模板文件
     */
    private String templateFile;


    public ExportAttachment(HttpServletRequest request) {
        if (request != null) {
            request.setAttribute(EXPORT_ATTACH_TO_REQUEST_KEY, this);
        }
    }

    public ExportAttachment(HttpServletRequest request, String fileType, String displayFileName,
                            String templateFile, ContentDisplayType contentDisplayType) {
        super();
        this.fileType = fileType;
        this.displayFileName = displayFileName;
        if (!this.displayFileName.contains(this.fileType)) {
            this.displayFileName += this.fileType;
        }

        this.templateFile = templateFile;
        this.contentDisplayType = contentDisplayType.value;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    public String getContentDisplayType() {
        return contentDisplayType;
    }

    public void setContentDisplayType(String contentDisplayType) {
        this.contentDisplayType = contentDisplayType;
    }

    /**
     * 产生Excel 的导出对象
     *
     * @param request
     * @param displayType
     * @param templateFile
     * @return
     */
    public static ExportAttachment generateXls(HttpServletRequest request, ContentDisplayType displayType, String displayFileName, String templateFile) {
        ExportAttachment attachInfo = new ExportAttachment(request);
        attachInfo.setContentDisplayType(displayType.getValue());
        attachInfo.setDisplayFileName(displayFileName);
        attachInfo.setFileType(EXPORT_XLS_SUFFIX);
        attachInfo.setTemplateFile(templateFile);
        return attachInfo;
    }

    public static ExportAttachment generatePdf(HttpServletRequest request, ContentDisplayType displayType, String displayFileName, String templateFile) {
        ExportAttachment attachInfo = new ExportAttachment(request);
        attachInfo.setContentDisplayType(displayType.getValue());
        attachInfo.setDisplayFileName(displayFileName);
        attachInfo.setFileType(EXPORT_PDF_SUFFIX);
        attachInfo.setTemplateFile(templateFile);
        return attachInfo;
    }

    public String getDisplayFileName() {
        return displayFileName;
    }

    public void setDisplayFileName(String displayFileName) {
        this.displayFileName = displayFileName;
    }


}
