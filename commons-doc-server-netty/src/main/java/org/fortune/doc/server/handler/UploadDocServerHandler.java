package org.fortune.doc.server.handler;

import org.apache.commons.lang3.StringUtils;
import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.account.Account;
import org.fortune.doc.common.domain.result.Result;
import org.fortune.doc.common.utils.FileUtil;
import org.fortune.doc.common.utils.ThumbUtil;
import org.fortune.doc.server.parse.RequestParam;
import org.jboss.netty.handler.codec.http.multipart.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author: landy
 * @date: 2019/6/2 21:25
 * @description: 文件上传操作
 */
public class UploadDocServerHandler extends AbstractDocServerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadDocServerHandler.class);

    private String fileName;
    private String dirPath;
    private String savePath;

    public UploadDocServerHandler(Account account) {
        super(account);
        createSaveDir();
    }

    public Result process(RequestParam reqParams) {
        FileUpload fileUpload = reqParams.getFileUpload();
        String srcFileName = reqParams.getFileName();
        if (StringUtils.isBlank(srcFileName)) {
            srcFileName = fileUpload.getFilename();
        }
        LOGGER.info("--srcFileName--" + srcFileName);

        this.fileName = generateFileNameOfTime(srcFileName);
        Result result = new Result();
        result.setAction(reqParams.getAction());
        File newFile = new File(getRealSavePath());
        try {
            boolean bool = fileUpload.renameTo(newFile);
            result.buildSuccess();
            result.setMsg("文件上传成功");
            LOGGER.info("文件上传成功,保存路径为:" + getSavePath() + ",真实路径为：" + getRealPath(getSavePath()));
            result.setFilePath(getSavePath());
            if ((bool) && (reqParams.getThumbMark().equals(Constants.THUMB_MARK_YES))) {
                createThumb();
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result.buildFailed();
            result.setMsg("存储文件报错" + e + ",acount:" + this.account);
            LOGGER.error("存储文件报错" + e + ",acount:" + this.account);
        }
        return result;
    }

    private String generateFileNameOfTime(String fileName) {
        return FileUtil.generateFileNameOfTime(fileName);
    }

    private void createThumb() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("生成缩略图");
        }
        String thumbFileName = ThumbUtil.getThumbImagePath(this.fileName);
        LOGGER.info("生成缩略图的名称为:" + thumbFileName + ",路径为:" + this.dirPath + thumbFileName);
        new ThumbUtil(new File(getRealSavePath()), new File(
                this.dirPath + thumbFileName), this.account.getThumbWidth(),
                this.account.getThumbHeight()).createThumbImage();
		/*ThumbUtil.createThumbImage(new File(getRealSavePath()), new File(
				this.dirPath + thumbFileName), this.account.getThumbWidth(),
				this.account.getThumbHeight());*/
    }

    private String getRealSavePath() {
        return this.dirPath + this.fileName;
    }

    private String getSavePath() {
        return this.savePath + this.fileName;
    }
    /**
     * 创建文件保存的目录
     *
     * @author Landy
     */
    private void createSaveDir() {
        this.savePath = FileUtil.generateFileSavePath(this.account);

        this.dirPath = (this.account.getRootPath() + this.savePath);

        File dirFolder = new File(this.dirPath);

        if (!dirFolder.exists())
            dirFolder.mkdirs();
    }
}
