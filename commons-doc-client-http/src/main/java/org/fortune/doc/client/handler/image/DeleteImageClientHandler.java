package org.fortune.doc.client.handler.image;

import org.fortune.doc.client.handler.AbstractDeleteDocClientHandler;
import org.fortune.doc.common.enums.DocOperationURI;

/**
 * @author: landy
 * @date: 2019/7/7 09:16
 * @description:
 */
public class DeleteImageClientHandler extends AbstractDeleteDocClientHandler {

    @Override
    protected DocOperationURI docOperationURI() {
        return DocOperationURI.IMAGE_DELETE;
    }
}
