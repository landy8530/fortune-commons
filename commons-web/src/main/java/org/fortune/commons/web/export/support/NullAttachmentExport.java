package org.fortune.commons.web.export.support;

import org.fortune.commons.web.export.AbstractAttachmentExport;
import org.fortune.commons.web.export.ExportAttachment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: Landy
 * @date: 2019/4/15 23:30
 * @description:
 */
public class NullAttachmentExport extends AbstractAttachmentExport {
    @Override
    public void export(HttpServletRequest request, HttpServletResponse response, ExportAttachment exportAttachment, Map data) {
        String fileType = exportAttachment.getFileType();
        LOGGER.error("导出的文件类型：" + fileType + ",找不到解析,转到空导出的处理");
    }

    @Override
    public boolean isSupport(ExportAttachment exportAttachment) {
        return true;
    }

    @Override
    public String supportFileType() {
        return "";
    }
}
