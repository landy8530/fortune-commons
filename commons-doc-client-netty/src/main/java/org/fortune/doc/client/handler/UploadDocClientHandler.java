package org.fortune.doc.client.handler;

import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.enums.DocOperationType;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

import java.io.File;
import java.net.URI;

/**
 * @author: landy
 * @date: 2019/5/30 23:27
 * @description: 客户端文件上传处理器
 */
public class UploadDocClientHandler extends AbstractDocClientHandler {

    private File file;
    private String thumbMark = Constants.THUMB_MARK_NO;
    private String fileName;

    public UploadDocClientHandler(String host, URI uri, File file,
                                   String fileName, String thumbMark, String userName, String pwd) {
        super(host, uri, userName, pwd);
        this.file = file;
        this.thumbMark = thumbMark;
        this.fileName = fileName;
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
                    DocOperationType.UPLOAD_FILE.getValue());
            //设置是否需要缩略图
            bodyRequestEncoder.addBodyAttribute(Constants.THUMB_MARK_KEY, this.thumbMark);
            //设置账户鉴权
            bodyRequestEncoder
                    .addBodyAttribute(Constants.USER_NAME_KEY, super.getUserName());
            bodyRequestEncoder.addBodyAttribute(Constants.PWD_KEY, super.getPwd());
            //设置文件名称
            bodyRequestEncoder.addBodyAttribute(Constants.FILE_NAME_KEY, this.fileName);
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
