package org.fortune.doc.server.handler;

import org.apache.commons.lang3.StringUtils;
import org.fortune.doc.common.domain.Account;
import org.fortune.doc.common.domain.Result;
import org.fortune.doc.common.enums.DocOperationType;
import org.fortune.doc.common.utils.ThumbUtil;
import org.fortune.doc.server.parse.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/6/2 21:15
 * @description: 图片缩略图上传操作
 */
public class CreateThumbPictureServerHandler extends AbstractDocServerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateThumbPictureServerHandler.class);

    public CreateThumbPictureServerHandler(Account account) {
        super(account);
    }

    public Result process(RequestParam reqParams) {
        Result result = new Result();
        result.setCode(false);
        result.setAction(DocOperationType.CREATE_THUMB_PICTURE.getValue());

        if (StringUtils.isNotBlank(reqParams.getFilePath())) {
            String realPath = getRealPath(reqParams.getFilePath());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("生成缩略图：" + realPath);
            }
            File file = new File(realPath);

            boolean bool = false;
            if (file.exists()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("生成缩略图");
                }
                String thumbFilePath = ThumbUtil
                        .getThumbImagePath(realPath);
                File thumbFile = new File(thumbFilePath);
                if (!thumbFile.exists()) {
                    new ThumbUtil(new File(realPath), thumbFile, this.account.getThumbWidth(),
                            this.account.getThumbHeight()).createThumbImage();
					/*ThumbUtil.createThumbImage(new File(realPath),
							thumbFile, this.account.getThumbWidth(),
							this.account.getThumbHeight());*/
                    result.setCode(bool);
                    result.setMsg("缩略图创建成功");
                } else {
                    result.setCode(bool);
                    result.setMsg("缩略图已存在，无法创建；缩略图路径=" + thumbFilePath);
                }
            }
        }

        return result;
    }
}
