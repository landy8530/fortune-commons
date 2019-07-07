package org.fortune.doc.client.handler.image;

import org.fortune.doc.client.handler.AbstractUploadDocClientHandler;
import org.fortune.doc.client.result.DocResponseContent;
import org.fortune.doc.common.domain.result.ImageDocResult;
import org.fortune.doc.common.enums.DocOperationURI;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/7/7 09:25
 * @description:
 */
public class UploadImageClientHandler extends AbstractUploadDocClientHandler {

    public ImageDocResult doUploadImage(String fileName, File file, String thumbMark) {
        DocResponseContent ret = super.doUpload(fileName, file, thumbMark);
        ImageDocResult result = super.convertToObject(ret.getContent(), ImageDocResult.class);
        return result;
    }

    public ImageDocResult doUploadImage(String fileName, byte[] content, String thumbMark) {
        DocResponseContent ret = super.doUpload(fileName, content, thumbMark);
        ImageDocResult result = super.convertToObject(ret.getContent(), ImageDocResult.class);
        return result;
    }

    @Override
    protected DocOperationURI docOperationURI() {
        return DocOperationURI.IMAGE_UPLOAD;
    }
}
