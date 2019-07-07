package org.fortune.doc.client.handler;

import org.fortune.doc.client.result.DocResponseContent;
import org.fortune.doc.client.support.DocClientWrapper;
import org.fortune.doc.common.domain.Constants;

/**
 * @author: landy
 * @date: 2019/7/7 10:13
 * @description:
 */
public abstract class AbstractDeleteDocClientHandler extends AbstractDocClientHandler {

    public DocResponseContent doDelete(String filePath) {
        DocClientWrapper hw = DocClientWrapper.newInstance();
        this.addCommonNV(hw);
        hw.addNV(Constants.FILE_PATH_KEY, filePath);
        return hw.doPost(docOperationURI().getUri());
    }

}
