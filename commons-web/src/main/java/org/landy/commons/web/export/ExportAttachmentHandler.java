package org.landy.commons.web.export;

import org.landy.commons.core.help.ApplicationContextHolder;
import org.landy.commons.web.export.support.NullAttachmentExport;
import org.landy.commons.web.export.support.PdfAttachmentExport;
import org.landy.commons.web.export.support.XlsAttachmentExport;
import org.landy.commons.web.loader.WebContextLoader;
import org.landy.commons.web.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Landy
 * @date: 2019/4/16 21:36
 * @description:
 */
public class ExportAttachmentHandler {
    public static final String BEAN_NAME = "exportAttachmentHandler";
    private static Logger LOGGER = LoggerFactory.getLogger(ExportAttachmentHandler.class);
    private AbstractAttachmentExport nullExportHandler = new NullAttachmentExport();

    private List<AbstractAttachmentExport> exportHandlers;

    public static ExportAttachmentHandler getInstance() {
        return ApplicationContextHolder.accessApplicationContext().getBean(BEAN_NAME,ExportAttachmentHandler.class);
    }

    public void init() {
        LOGGER.info("导出模块初始化，并加入默认的Handler *****************start");
        exportHandlers = new ArrayList<>();
        exportHandlers.add(new XlsAttachmentExport());
        exportHandlers.add(new PdfAttachmentExport());
        LOGGER.info("导出模块初始化 *****************end");
    }

    private AbstractAttachmentExport getExportHandler(ExportAttachment attachInfo) {
        for (AbstractAttachmentExport item : exportHandlers) {
            if (item.isSupport(attachInfo)) {
                return item;
            }
        }
        return nullExportHandler;
    }


    public void export(HttpServletRequest request,
                       HttpServletResponse response, String defaultfileName, Map data) {

        ExportAttachment attachInfo = ExportUtils.getExportAttachInfo(request);

        String fileType = attachInfo.getFileType();

        defaultfileName = defaultfileName.replaceAll("\\.(jsp|JSP)", fileType);
        //获取模板
        String templateFilePath = ExportUtils.getExportTemplateFilePath(attachInfo, defaultfileName);//request.getParameter(MyWebStaticConstants.PARAM_EXPORT_XLS_FILE_NAME);

        String pageRootPath = WebContextLoader.getInstance().getPageRootPath();
        if (!templateFilePath.contains(pageRootPath)) {
            templateFilePath = PathUtils.getFilePath(pageRootPath, templateFilePath);
        }

        LOGGER.info("导出的文件模板路径：" + templateFilePath);
        attachInfo.setTemplateFile(templateFilePath);

        AbstractAttachmentExport exportHandler = this.getExportHandler(attachInfo);
        exportHandler.export(request, response, attachInfo, data);

    }
}
