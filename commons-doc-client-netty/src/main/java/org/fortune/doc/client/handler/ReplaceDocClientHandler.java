package org.fortune.doc.client.handler;

import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.enums.DocOperationType;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

import java.io.File;
import java.net.URI;

/**
 * @author: landy
 * @date: 2019/5/30 23:30
 * @description: 客户端替换文件处理器
 */
public class ReplaceDocClientHandler extends AbstractDocClientHandler {

    private File file;
    private String filePath;

    public ReplaceDocClientHandler(String host, URI uri, String filePath,
                                    File file, String userName, String pwd) {
        super(host, uri, userName, pwd);
        this.filePath = filePath;
        this.file = file;
    }

    public HttpPostRequestEncoder wrapRequestData(HttpDataFactory factory) {
        HttpPostRequestEncoder bodyRequestEncoder = null;
        try {
            bodyRequestEncoder = new HttpPostRequestEncoder(factory,
                    getRequest(), true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
            e.printStackTrace();
        }
        try {
            //设置请求方式post
            bodyRequestEncoder.addBodyAttribute("getform", "POST");
            //设置文件操作类型为文件上传
            bodyRequestEncoder.addBodyAttribute(Constants.ACTION_KEY,
                    DocOperationType.REPLACE_FILE.getValue());
            //设置是否需要缩略图
            bodyRequestEncoder.addBodyAttribute(Constants.THUMB_MARK_KEY, Constants.THUMB_MARK_YES);
            //设置账户鉴权
            bodyRequestEncoder
                    .addBodyAttribute(Constants.USER_NAME_KEY, super.getUserName());
            bodyRequestEncoder.addBodyAttribute(Constants.PWD_KEY, super.getPwd());
            //设置文件名称
            bodyRequestEncoder.addBodyAttribute(Constants.FILE_PATH_KEY, this.filePath);
            //设置文件内容
            bodyRequestEncoder.addBodyFileUpload("myfile", this.file,
                    "application/x-zip-compressed", false);
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
