package org.fortune.doc.server.parse;

import org.fortune.doc.common.domain.Constants;
import org.jboss.netty.handler.codec.http.multipart.Attribute;
import org.jboss.netty.handler.codec.http.multipart.FileUpload;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.util.List;

/**
 * @author: landy
 * @date: 2019/6/2 21:04
 * @description:
 */
public class RequestParamParser {

    public static void parseParams(HttpPostRequestDecoder decoder,
                                   RequestParam requestParams) {
        if (decoder == null) {
            return;
        }
        if (requestParams == null)
            requestParams = new RequestParam();
        try {
            List<InterfaceHttpData> datas = decoder.getBodyHttpDatas();
            if (datas != null) {
                for (InterfaceHttpData data : datas)
                    if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                        //接收到的是普通参数
                        Attribute attribute = (Attribute) data;
                        String value = attribute.getValue();
                        String name = attribute.getName();

                        if (Constants.PWD_KEY.equals(name))
                            requestParams.setPwd(value);
                        else if (Constants.USER_NAME_KEY.equals(name))
                            requestParams.setUserName(value);
                        else if (Constants.THUMB_MARK_KEY.equals(name))
                            requestParams.setThumbMark(value);
                        else if (Constants.ACTION_KEY.equals(name))
                            requestParams.setAction(value);
                        else if (Constants.FILE_PATH_KEY.equals(name))
                            requestParams.setFilePath(value);
                        else if (Constants.FILE_NAME_KEY.equals(name))
                            requestParams.setFileName(value);
                        else
                            requestParams.putOtherParam(name, value);
                    } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                        //接收到的是文件
                        FileUpload fileUpload = (FileUpload) data;
                        if (fileUpload.isCompleted()) {
                            requestParams.setFileUpload(fileUpload);
                            requestParams.setFileContentType(fileUpload.getContentType());
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
