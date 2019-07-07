package org.fortune.doc.client.handler;

import org.fortune.doc.client.result.DocResponseContent;
import org.fortune.doc.client.support.DocClientWrapper;
import org.fortune.doc.common.domain.Constants;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/7/7 10:13
 * @description:
 */
public abstract class AbstractReplaceDocClientHandler extends AbstractDocClientHandler {

    private DocClientWrapper doInReplace(String filePath) {
        DocClientWrapper hw = DocClientWrapper.newInstance();
        super.addCommonNV(hw);
        hw.addNV(Constants.FILE_PATH_KEY, filePath);
        return hw;
    }

    protected DocResponseContent doReplace(String filePath, File file) {
        DocClientWrapper hw =  doInReplace(filePath);
        super.addContent(hw, file);
        DocResponseContent ret = hw.doPost(docOperationURI().getUri());
        return ret;
    }

    protected DocResponseContent doReplace(String filePath, byte[] content) {
        DocClientWrapper hw = doInReplace(filePath);
        super.addContent(hw, content);
        DocResponseContent ret = hw.doPost(docOperationURI().getUri());
        return ret;
    }

}
