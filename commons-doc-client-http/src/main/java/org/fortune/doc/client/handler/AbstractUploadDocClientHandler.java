package org.fortune.doc.client.handler;

import org.apache.commons.lang3.StringUtils;
import org.fortune.doc.client.result.DocResponseContent;
import org.fortune.doc.client.support.DocClientWrapper;
import org.fortune.doc.common.domain.Constants;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/7/7 10:30
 * @description:
 */
public abstract class AbstractUploadDocClientHandler extends AbstractDocClientHandler {

    private DocClientWrapper doInUpload(String fileName, String thumbMark) {
        DocClientWrapper hw = DocClientWrapper.newInstance();
        super.addCommonNV(hw);
        hw.addNV(Constants.FILE_NAME_KEY, fileName);
        if (StringUtils.isNotBlank(thumbMark)) {
            hw.addNV(Constants.THUMB_MARK_KEY, thumbMark);
        }
        return hw;
    }

    protected DocResponseContent doUpload(String fileName, File file, String thumbMark) {
        DocClientWrapper hw = doInUpload(fileName, thumbMark);
        super.addContent(hw, file);
        DocResponseContent ret = hw.doPost(docOperationURI().getUri());
        return ret;
    }

    protected DocResponseContent doUpload(String fileName, byte[] content, String thumbMark) {
        DocClientWrapper hw = doInUpload(fileName, thumbMark);
        super.addContent(hw, content);
        DocResponseContent ret = hw.doPost(docOperationURI().getUri());
        return ret;
    }

}
