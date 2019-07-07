package org.fortune.doc.client.handler;

import com.alibaba.fastjson.JSON;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.fortune.doc.client.DocClientContainer;
import org.fortune.doc.client.support.DocClientWrapper;
import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.enums.DocOperationURI;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/7/5 23:59
 * @description:
 */
public abstract class AbstractDocClientHandler {

    public String getAccount() {
        return DocClientContainer.getInstance().getUserName();
    }

    public String getPassword() {
        return DocClientContainer.getInstance().getPassword();
    }

    protected void addCommonNV(DocClientWrapper hw) {
        hw.addNV(Constants.USER_NAME_KEY, getAccount());
        hw.addNV(Constants.PWD_KEY, getPassword());
    }

    protected void addContent(DocClientWrapper hw, File file) {
        FileBody fileBody = new FileBody(file);
        hw.getContentBodies().put(Constants.FILE_DATA_KEY, fileBody);
    }

    protected void addContent(DocClientWrapper hw, byte[] content) {
        ByteArrayBody byteArrayBody = new ByteArrayBody(content, Constants.FILE_DATA_KEY);
        hw.getContentBodies().put(Constants.FILE_DATA_KEY, byteArrayBody);
    }

    protected <T> T convertToObject(String resultContent, Class<T> clazz) {
        if (StringUtils.isEmpty(resultContent)) {
            new Exception("解析内容为空");
        }
        return JSON.parseObject(resultContent, clazz);
    }

    protected abstract DocOperationURI docOperationURI();

}
