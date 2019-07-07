package org.fortune.doc.client.handler.attachment;

import org.fortune.doc.client.handler.AbstractReplaceDocClientHandler;
import org.fortune.doc.client.result.DocResponseContent;
import org.fortune.doc.common.domain.result.AttachDocResult;
import org.fortune.doc.common.enums.DocOperationURI;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/7/7 09:01
 * @description:
 */
public class ReplaceAttachmentClientHandler extends AbstractReplaceDocClientHandler {

    public AttachDocResult doReplaceDoc(String filePath, File file) {
        DocResponseContent ret = super.doReplace(filePath, file);
        return super.convertToObject(ret.getContent(), AttachDocResult.class);
    }

    public AttachDocResult doReplaceDoc(String filePath, byte[] content) {
        DocResponseContent ret = super.doReplace(filePath, content);
        return super.convertToObject(ret.getContent(), AttachDocResult.class);
    }

    @Override
    protected DocOperationURI docOperationURI() {
        return DocOperationURI.ATTACHMENT_REPLACE;
    }
}
