package org.fortune.doc.server.handler;

import org.apache.commons.lang3.StringUtils;
import org.fortune.doc.common.domain.account.Account;
import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.result.Result;
import org.fortune.doc.common.enums.DocOperationType;
import org.fortune.doc.server.parse.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/6/2 21:24
 * @description: 文件删除操作
 */
public class DeleteDocServerHandler extends AbstractDocServerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDocServerHandler.class);

    public DeleteDocServerHandler(Account account) {
        super(account);
    }

    public Result process(RequestParam reqParams) {
        Result result = new Result();
        result.buildFailed();
        result.setAction(DocOperationType.DELETE_FILE.getValue());

        if (StringUtils.isNotBlank(reqParams.getFilePath())) {
            String realPath = getRealPath(reqParams.getFilePath());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("进行删除文件：" + realPath);
            }
            File file = new File(realPath);

            boolean bool = false;
            if ((file.exists()) && (file.isFile())) {
                bool = file.delete();
            }

            int position = realPath.lastIndexOf(".");
            String suffix = realPath.substring(position);
            String thumbPath = realPath.substring(0, position) + Constants.THUMB_SUFFIX
                    + suffix;

            File thumbFile = new File(thumbPath);

            if ((thumbFile.exists()) && (thumbFile.isFile()) && (bool)) {
                thumbFile.delete();
            }
            result.buildSuccess();
            result.setMsg("文件删除成功");
        }
        if (!result.isSuccess()) {
            result.setMsg("文件删除失败");
        }
        return result;
    }
}
