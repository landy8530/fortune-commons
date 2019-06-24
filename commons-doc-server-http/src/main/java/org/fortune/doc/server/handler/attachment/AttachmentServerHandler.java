package org.fortune.doc.server.handler.attachment;

import org.fortune.doc.server.DocServerContainer;
import org.fortune.doc.server.handler.DocServerHandler;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/6/24 22:58
 * @description:
 */
public class AttachmentServerHandler extends DocServerHandler {

    public String getAttachmentRootPath() {
        return super.getRootPath() + File.separator + DocServerContainer.getInstance().getAttachmentBasePath();
    }

    @Override
    protected String getRealPath(String filePath) {
        String realPath = this.getAttachmentRootPath();
        if (realPath.endsWith(File.separator)) {
            realPath = realPath + filePath;
        } else {
            realPath = realPath + File.separator + filePath;
        }
        return realPath;
    }
}
