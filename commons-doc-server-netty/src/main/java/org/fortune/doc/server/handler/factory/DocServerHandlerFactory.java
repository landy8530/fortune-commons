package org.fortune.doc.server.handler.factory;

import org.apache.commons.lang3.StringUtils;
import org.fortune.doc.common.domain.Account;
import org.fortune.doc.common.domain.Result;
import org.fortune.doc.common.enums.DocOperationType;
import org.fortune.doc.common.utils.JsonUtil;
import org.fortune.doc.server.DocServerContainer;
import org.fortune.doc.server.handler.*;
import org.fortune.doc.server.parse.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: landy
 * @date: 2019/6/2 21:54
 * @description:
 */
public class DocServerHandlerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocServerHandlerFactory.class);

    /**
     * 根据请求的不同参数，进行相应的文件处理操作
     * @param requestParams
     * @return
     * @author:landyChris
     */
    public static String process(RequestParam requestParams) {
        Account account = DocServerContainer.getInstance().getAccount(
                requestParams.getUserName());
        DocOperationType action = DocOperationType.fromValue(requestParams
                .getAction());
        Result result = null;
        if (account.auth(requestParams.getPwd())) {
            DocServerProcessor handler = null;
            if (DocOperationType.UPLOAD_FILE == action) {//上传文件
                if (requestParams.getFileUpload() != null) {
                    LOGGER.info("进行文件上传操作....");
                    handler = new UploadDocServerHandler(account);
                }
            } else if (DocOperationType.DELETE_FILE == action) {//删除文件
                if (StringUtils.isNotBlank(requestParams.getFilePath())) {
                    LOGGER.info("进行文件删除操作....");
                    handler = new DeleteDocServerHandler(account);
                }
            } else if (DocOperationType.REPLACE_FILE == action) {//替换文件
                if ((requestParams.getFileUpload() != null)
                        && (StringUtils.isNotBlank(requestParams.getFilePath()))) {
                    LOGGER.info("进行文件替换操作....");
                    handler = new ReplaceDocServerHandler(account);
                }
            } else if ((DocOperationType.CREATE_THUMB_PICTURE == action)
                    && (StringUtils.isNotBlank(requestParams.getFilePath()))) {//生成缩略图
                LOGGER.info("进行生成缩略图操作....");
                handler = new CreateThumbPictureServerHandler(account);
            }
            if(handler != null) {
                result = handler.process(requestParams);
            }
        } else {
            result = new Result();
            result.setAction(DocOperationType.NULL.getValue());
            result.setCode(false);
            result.setMsg("密码错误");
        }
        if (result == null) {
            result = new Result();
            result.setAction(DocOperationType.NULL.getValue());
            result.setCode(false);
            result.setMsg("无效动作");
        }

        String json = JsonUtil.toJSONString(result);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("执行结果:" + json);
        }
        return json;
    }

}
