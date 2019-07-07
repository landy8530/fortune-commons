package org.fortune.doc.server.handler;

import org.apache.commons.lang3.StringUtils;
import org.fortune.doc.common.domain.account.Account;
import org.fortune.doc.common.domain.result.Result;
import org.fortune.doc.common.enums.DocOperationType;
import org.fortune.doc.common.utils.ThumbUtil;
import org.fortune.doc.server.parse.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/6/2 21:24
 * @description: 文件替换操作
 */
public class ReplaceDocServerHandler extends AbstractDocServerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReplaceDocServerHandler.class);

    public ReplaceDocServerHandler(Account account) {
        super(account);
    }

    public Result process(RequestParam reqParams) {
        Result result = new Result();
        result.buildFailed();
        result.setAction(DocOperationType.REPLACE_FILE.getValue());

        if ((StringUtils.isNotBlank(reqParams.getFilePath()))
                && (reqParams.getFileUpload() != null)) {
            String realPath = getRealPath(reqParams.getFilePath());


            LOGGER.info("进行替换文件：" + realPath);
            File oldFile = new File(realPath);
            if ((oldFile.exists()) && (oldFile.isFile())) {
                oldFile.delete();
            } else {
                result.setMsg("替换的文件不存在");
                LOGGER.info("替换的文件不存在：" + realPath);
                return result;
            }

            String thumbFilePath = ThumbUtil.getThumbImagePath(realPath);
            File thumbFile = new File(thumbFilePath);

            boolean thumbBool = false;
            if ((thumbFile.exists()) && (thumbFile.isFile())) {
                thumbFile.delete();
                thumbBool = true;
            }
            try {
                boolean bool = reqParams.getFileUpload().renameTo(oldFile);
                result.buildSuccess();
                result.setMsg("文件替换上传成功");
                LOGGER.info("文件替换上传成功");
                result.setFilePath(reqParams.getFilePath());
                if ((bool) && (thumbBool)) {
                    LOGGER.info("生成缩略图");

                    new ThumbUtil(oldFile, thumbFile, this.account.getThumbWidth(),
                            this.account.getThumbHeight()).createThumbImage();
					/*ThumbUtil.createThumbImage(oldFile, thumbFile,
							this.account.getThumbWidth(),
							this.account.getThumbHeight());*/
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.buildFailed();
                result.setMsg("文件替换报错" + e + ",acount:" + this.account);
                LOGGER.error("文件替换报错" + e + ",acount:" + this.account);
            }
        }
        return result;
    }
}
