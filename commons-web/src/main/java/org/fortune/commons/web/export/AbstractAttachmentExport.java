package org.fortune.commons.web.export;

import org.fortune.commons.web.loader.WebContextLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: Landy
 * @date: 2019/4/15 23:13
 * @description:
 */
public abstract class AbstractAttachmentExport {

    protected static Logger LOGGER = LoggerFactory.getLogger(AbstractAttachmentExport.class);

    /**
     * @param request
     * @param response
     * @param exportAttachment
     * @param data
     */
    public abstract void export(HttpServletRequest request, HttpServletResponse response, ExportAttachment exportAttachment, Map data);

    /**
     * 是否支持导出
     *
     * @param exportAttachment
     * @return
     */
    public abstract boolean isSupport(ExportAttachment exportAttachment);

    /**
     * 支撑的文件类型
     *
     * @return
     */
    public abstract String supportFileType();

    /**
     * 获取根路径
     *
     * @param request
     * @return
     */
    protected String getRootPath(HttpServletRequest request) {
        return WebContextLoader.getInstance().getRootPath();
    }

}
