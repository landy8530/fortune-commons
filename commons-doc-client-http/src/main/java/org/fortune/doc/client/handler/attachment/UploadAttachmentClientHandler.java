package org.fortune.doc.client.handler.attachment;

import org.fortune.doc.client.handler.AbstractUploadDocClientHandler;
import org.fortune.doc.client.result.DocResponseContent;
import org.fortune.doc.common.domain.result.AttachDocResult;
import org.fortune.doc.common.enums.DocOperationURI;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/7/7 09:13
 * @description:
 */
public class UploadAttachmentClientHandler extends AbstractUploadDocClientHandler {

    public AttachDocResult doUploadDoc(String fileName, File file, String thumbMark) {
        DocResponseContent ret = super.doUpload(fileName, file, thumbMark);
        AttachDocResult result = super.convertToObject(ret.getContent(), AttachDocResult.class);
        return result;
    }

    public AttachDocResult doUploadDoc(String fileName, byte[] content, String thumbMark) {
        DocResponseContent ret = super.doUpload(fileName, content, thumbMark);
        AttachDocResult result = super.convertToObject(ret.getContent(), AttachDocResult.class);
        return result;
    }

    @Override
    protected DocOperationURI docOperationURI() {
        return DocOperationURI.ATTACHMENT_UPLOAD;
    }
}
