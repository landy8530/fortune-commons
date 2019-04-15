package org.landy.commons.web.export.support;

import org.apache.commons.lang3.StringUtils;
import org.landy.commons.export.excel.jxls.ExportXlsHandler;
import org.landy.commons.web.export.AbstractAttachmentExport;
import org.landy.commons.web.export.ExportAttachment;
import org.landy.commons.web.util.PathUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author: Landy
 * @date: 2019/4/15 23:31
 * @description:
 */
public class XlsAttachmentExport extends AbstractAttachmentExport {
    @Override
    public void export(HttpServletRequest request, HttpServletResponse response, ExportAttachment exportAttachment, Map data) {
        OutputStream out=null;
        try {
            String filePath=exportAttachment.getTemplateFile();
            String templateFullFilePath = PathUtils.getFilePath(getRootPath(request) , filePath);
            LOGGER.info("导出的文件模板全路径：" + templateFullFilePath);
            String exportFileName = exportAttachment.getDisplayFileName();
            ExportXlsHandler exporcess = new ExportXlsHandler();
            response.setContentType("application/msexcel;charset=utf-8");
            String attach = exportAttachment.getContentDisplayType();

            response.setHeader("Content-Disposition", attach + "; filename=\""+ java.net.URLEncoder.encode(exportFileName, "UTF-8"));
            out = response.getOutputStream();
            exporcess.execute(out, templateFullFilePath, data);
        } catch (Throwable e) {
            LOGGER.error("导出EXCEL出错：" + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            if(out!=null){
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    @Override
    public boolean isSupport(ExportAttachment exportAttachment) {
        return StringUtils.equals(exportAttachment.getFileType(), supportFileType());
    }

    @Override
    public String supportFileType() {
        return ".xls";
    }
}
