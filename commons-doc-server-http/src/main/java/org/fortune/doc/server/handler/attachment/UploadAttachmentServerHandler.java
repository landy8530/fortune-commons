package org.fortune.doc.server.handler.attachment;

import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.account.DocAccountBean;
import org.fortune.doc.common.domain.result.AttachDocResult;
import org.fortune.doc.common.utils.FileUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: landy
 * @date: 2019/6/24 23:03
 * @description:
 */
@Component
public class UploadAttachmentServerHandler extends AttachmentServerHandler {

    public AttachDocResult doUpload(HttpServletRequest request) {
        AttachDocResult result = new AttachDocResult();
        DocAccountBean accountBean = super.getAccount(request);
        if (accountBean == null) {
            result.buildFailed();
            result.buildCustomMsg("账号信息不对，请重新确认");
            return result;
        } else {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest mreqeust = (MultipartHttpServletRequest)request;
                String fileName = mreqeust.getParameter(Constants.FILE_NAME_KEY);
                MultipartFile file = mreqeust.getFile(Constants.FILE_DATA_KEY);
                if (!file.isEmpty()) {
                    try {
                        String rootPath = super.getAttachmentRootPath();
                        LOGGER.info("文件上传的根目录:{}",rootPath);
                        this.checkRootPath(rootPath);
                        String savePath = super.getAttachmentBasePath() + File.separator + FileUtil.generateFileSavePath(accountBean);
                        LOGGER.info("文件上传需要保存到数据库的目录:{}",savePath);
                        String dirPath = super.getRootPath() + File.separator + savePath;
                        LOGGER.info("文件上传绝对路径:{}",dirPath);
                        String newFileName = FileUtil.generateFileNameOfTime(fileName);
                        LOGGER.info("文件上传动态生成的图片文件名称:{}",newFileName);
                        this.checkStorePath(dirPath);
                        File uploadedFile3 = new File(dirPath, newFileName);
                        file.transferTo(uploadedFile3);
                        result.setFilePath(savePath + File.separator + newFileName);
                        result.buildSuccess();
                        LOGGER.info("文件上传成功,路径为{}",result.getFilePath());
                    } catch (Exception ex) {
                        result.buildFailed();
                        LOGGER.error("文件上传失败", ex);
                    }
                } else {
                    result.buildFailed();
                    LOGGER.error("文件上传失败,上传的文件未提供");
                }
            }
            return result;
        }
    }

}
