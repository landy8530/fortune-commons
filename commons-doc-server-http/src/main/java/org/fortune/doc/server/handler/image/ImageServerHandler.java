package org.fortune.doc.server.handler.image;

import org.fortune.doc.server.DocServerContainer;
import org.fortune.doc.server.handler.DocServerHandler;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/6/24 22:54
 * @description:
 */
public class ImageServerHandler extends DocServerHandler {

    public String getImageRootPath() {
        return super.getRootPath() + File.separator + DocServerContainer.getInstance().getImagesBasePath();
    }

    @Override
    protected String getRealPath(String filePath) {
        String realPath = this.getImageRootPath();
        if (realPath.endsWith(File.separator)) {
            realPath = realPath + filePath;
        } else {
            realPath = realPath + File.separator + filePath;
        }
        return realPath;
    }
}
