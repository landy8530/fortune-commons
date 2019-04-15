package org.landy.commons.web.export.support;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.landy.commons.core.freemarker.FortuneFreemarkerResolver;
import org.landy.commons.core.freemarker.FtlResourceData;
import org.landy.commons.export.pdf.itextpdf.FortuneXMLWorkerHelper;
import org.landy.commons.web.export.AbstractAttachmentExport;
import org.landy.commons.web.export.ExportAttachment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Map;

/**
 * @author: Landy
 * @date: 2019/4/15 23:31
 * @description:
 */
public class PdfAttachmentExport extends AbstractAttachmentExport {
    @Override
    public void export(HttpServletRequest request, HttpServletResponse response, ExportAttachment exportAttachment, Map data) {
        OutputStream out = null;
        StringReader reader = null;
        Document document = new Document();
        try {

            FtlResourceData rsd = new FtlResourceData();
            rsd.setData(data);
            rsd.setFtlTemplatePath(exportAttachment.getTemplateFile());
            String content = FortuneFreemarkerResolver.getInstance().process(rsd);
            reader = new StringReader(content);

            String exportFileName = exportAttachment.getDisplayFileName();
            response.setContentType("application/pdf;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            String attach = exportAttachment.getContentDisplayType();

            response.setHeader("Content-Disposition", attach + "; filename=\"" + java.net.URLEncoder.encode(exportFileName, "UTF-8"));
            out = response.getOutputStream();
            PdfWriter pdfwriter = PdfWriter.getInstance(document, out);
            document.open();
            FortuneXMLWorkerHelper.getInstance().parseXHtml(pdfwriter, document, reader);


        } catch (Throwable e) {
            LOGGER.error("导出EXCEL出错：" + e.getLocalizedMessage(), e);
        } finally {
            if (document != null) {
                document.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (out != null) {
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
        return ".pdf";
    }
}
