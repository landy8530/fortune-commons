package org.landy.commons.web.export;

import org.landy.commons.core.constants.Constants;
import org.landy.commons.web.util.PathUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Landy
 * @date: 2019/4/15 23:20
 * @description:
 */
public class ExportUtils {

    /**
     * 获取导出附件的信息
     *
     * @param request
     * @return
     */
    public static ExportAttachment getExportAttachInfo(HttpServletRequest request) {
        ExportAttachment attach;
        Object object = request.getAttribute(ExportAttachment.EXPORT_ATTACH_TO_REQUEST_KEY);
        if (object != null) {
            attach = (ExportAttachment) object;
        } else {
            attach = ExportAttachment.NULL_ATTACH_INFO;
        }
        return attach;
    }

    /**
     * 判断是否是导出操作,以导出的文件类型为主，如果导出的文件类型不为空说明有导出操作
     * <br/>
     * exportFileType;从getParameter或getAttribute中获取
     * <br/>
     *
     * @param request
     * @return true，有导出操作，false 没有
     */
    public static boolean isExport(HttpServletRequest request) {
        ExportAttachment attach = getExportAttachInfo(request);
        return attach != ExportAttachment.NULL_ATTACH_INFO;
    }

    /**
     * 获取导出附件模板的路径
     *
     * @param attach
     * @param defaultFilePath
     * @return
     */
    public static String getExportTemplateFilePath(ExportAttachment attach, String defaultFilePath) {
        String exportFileName = null;
        if (attach != null) {
            exportFileName = attach.getTemplateFile();
        }

        if (StringUtils.isEmpty(exportFileName)) {
            exportFileName = defaultFilePath;
            if (exportFileName.lastIndexOf(Constants.SLASH) > 0) {
                exportFileName = exportFileName.substring(exportFileName.lastIndexOf(Constants.SLASH) + 1);
            }
        } else {
            String path = defaultFilePath.substring(0, defaultFilePath.lastIndexOf(Constants.SLASH) + 1);
            exportFileName = PathUtils.getFilePath(path, exportFileName);

        }
        return exportFileName;
    }

}
