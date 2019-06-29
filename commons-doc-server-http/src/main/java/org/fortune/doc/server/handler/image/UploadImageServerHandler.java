package org.fortune.doc.server.handler.image;

import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.account.DocAccountBean;
import org.fortune.doc.common.domain.account.ImageDocThumbBean;
import org.fortune.doc.common.domain.result.ImageDocResult;
import org.fortune.doc.common.utils.FileUtil;
import org.fortune.doc.server.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * @author: landy
 * @date: 2019/6/16 14:23
 * @description:
 */
@Component
public class UploadImageServerHandler extends ImageServerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadImageServerHandler.class);

    public ImageDocResult doUpload(HttpServletRequest request) {
        ImageDocResult result = new ImageDocResult();
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
                        String rootPath = super.getImageRootPath();
                        LOGGER.info("图片上传的根目录:{}",rootPath);
                        this.checkRootPath(rootPath);
                        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                        String savePath = super.getImageBasePath() + File.separator + FileUtil.generateFileSavePath(accountBean);
                        LOGGER.info("图片上传需要保存到数据库的目录:{}",savePath);
                        String dirPath = super.getRootPath() + File.separator + savePath;
                        LOGGER.info("图片上传绝对路径:{}",dirPath);
                        String newFileName = FileUtil.generateFileNameOfTime(fileName);
                        LOGGER.info("图片上传动态生成的图片文件名称:{}",newFileName);
                        String newFileName0 = newFileName.substring(0, newFileName.lastIndexOf("."));
                        this.checkStorePath(dirPath);
                        File uploadedFile3 = new File(dirPath, newFileName);
                        file.transferTo(uploadedFile3);
                        String thumbMark = request.getParameter(Constants.THUMB_MARK_KEY);
                        if (Constants.THUMB_MARK_VAL.equals(thumbMark)) {
                            List<ImageDocThumbBean> thumbBeans = accountBean.getThumbConfig();
                            if (thumbBeans != null) {
                                Iterator thumbBean = thumbBeans.iterator();
                                while(thumbBean.hasNext()) {
                                    ImageDocThumbBean thumb = (ImageDocThumbBean)thumbBean.next();
                                    File uploadedFile1 = new File(dirPath, newFileName0 + thumb.getSuffix() + "." + fileExt);
                                    LOGGER.info("图片上传动态生成的缩略图图片文件:{}",uploadedFile1);
                                    ImageUtils.ratioZoom2(uploadedFile3, uploadedFile1, thumb.getRatio());
                                }
                            }
                        }
                        result.setFilePath(savePath + File.separator + newFileName);
                        result.buildSuccess();
                        LOGGER.info("图片上传成功,路径为{}",result.getFilePath());
                    } catch (Exception ex) {
                        result.buildFailed();
                        LOGGER.error("图片上传失败", ex);
                    }
                } else {
                    result.buildFailed();
                    LOGGER.error("图片上传失败,上传的文件未提供");
                }
            }
            return result;
        }
    }

}
