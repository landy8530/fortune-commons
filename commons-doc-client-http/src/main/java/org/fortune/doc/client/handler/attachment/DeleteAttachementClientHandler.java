package org.fortune.doc.client.handler.attachment;

import org.fortune.doc.client.handler.AbstractDeleteDocClientHandler;
import org.fortune.doc.common.enums.DocOperationURI;

/**
 * @author: landy
 * @date: 2019/7/7 08:59
 * @description:
 */
public class DeleteAttachementClientHandler extends AbstractDeleteDocClientHandler {

    @Override
    protected DocOperationURI docOperationURI() {
        return DocOperationURI.ATTACHMENT_DELETE;
    }
}
