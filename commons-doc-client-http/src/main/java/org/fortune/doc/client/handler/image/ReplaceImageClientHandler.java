package org.fortune.doc.client.handler.image;

import org.fortune.doc.client.handler.AbstractReplaceDocClientHandler;
import org.fortune.doc.client.result.DocResponseContent;
import org.fortune.doc.common.domain.result.ImageDocResult;
import org.fortune.doc.common.enums.DocOperationURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/7/7 09:17
 * @description:
 */
public class ReplaceImageClientHandler extends AbstractReplaceDocClientHandler {
    private static final Logger logger = LoggerFactory.getLogger(ReplaceImageClientHandler.class);

    public ImageDocResult doReplaceImage(String filePath, File file) {
        DocResponseContent ret = super.doReplace(filePath, file);
        if (ret == null) {
            logger.error("doReplace returned null for filePath: " + filePath);
            return null;
        }
        if (ret.getContent() == null) {
            logger.error("DocResponseContent.getContent() is null for filePath: " + filePath);
            return null;
        }
        return super.convertToObject(ret.getContent(), ImageDocResult.class);
    }

    public ImageDocResult doReplaceImage(String filePath, byte[] content) {
        DocResponseContent ret = super.doReplace(filePath, content);
        if (ret == null) {
            logger.error("doReplace returned null for filePath: " + filePath);
            return null;
        }
        if (ret.getContent() == null) {
            logger.error("DocResponseContent.getContent() is null for filePath: " + filePath);
            return null;
        }
        return super.convertToObject(ret.getContent(), ImageDocResult.class);
    }

    @Override
    protected DocOperationURI docOperationURI() {
        return DocOperationURI.IMAGE_REPLACE;
    }
}
