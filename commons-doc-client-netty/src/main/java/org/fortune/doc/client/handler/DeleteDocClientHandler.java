package org.fortune.doc.client.handler;

import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.enums.DocOperationType;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

import java.net.URI;

/**
 * @author: landy
 * @date: 2019/5/30 23:29
 * @description:
 */
public class DeleteDocClientHandler extends AbstractDocClientHandler {
    private String filePath;

    public DeleteDocClientHandler(String host, URI uri, String filePath,
                                   String userName, String pwd) {
        super(host, uri, userName, pwd);
        this.filePath = filePath;
    }

    public HttpPostRequestEncoder wrapRequestData(HttpDataFactory factory) {
        HttpPostRequestEncoder bodyRequestEncoder = null;
        try {
            bodyRequestEncoder = new HttpPostRequestEncoder(factory,
                    getRequest(), false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
            e.printStackTrace();
        }
        try {
            //设置请求方式
            bodyRequestEncoder.addBodyAttribute("getform", "POST");
            //设置文件操作类型
            bodyRequestEncoder.addBodyAttribute(Constants.ACTION_KEY,
                    DocOperationType.DELETE_FILE.getValue());
            //设置文件路径
            bodyRequestEncoder.addBodyAttribute(Constants.FILE_PATH_KEY, this.filePath);
            //鉴权
            bodyRequestEncoder
                    .addBodyAttribute(Constants.USER_NAME_KEY, super.getUserName());
            bodyRequestEncoder.addBodyAttribute(Constants.PWD_KEY, super.getPwd());
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
            e.printStackTrace();
        }
        try {
            bodyRequestEncoder.finalizeRequest();
        } catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
            e.printStackTrace();
        }
        return bodyRequestEncoder;
    }
}
