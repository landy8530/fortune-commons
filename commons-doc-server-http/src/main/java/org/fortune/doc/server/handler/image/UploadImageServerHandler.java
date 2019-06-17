package org.fortune.doc.server.handler.image;

import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.account.DocAccountBean;
import org.fortune.doc.common.domain.account.ImageDocThumbBean;
import org.fortune.doc.common.domain.result.ImageDocResult;
import org.fortune.doc.server.handler.DocServerHandler;
import org.fortune.doc.server.util.ImageUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author: landy
 * @date: 2019/6/16 14:23
 * @description:
 */
@Component
public class UploadImageServerHandler extends DocServerHandler {

    private void checkStorePath(String storePath) {
        File saveDirFile = new File(storePath);
        if (!saveDirFile.exists()) {
            saveDirFile.mkdirs();
        }
    }

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
                        this.checkRootPath(rootPath);
                        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                        String newFileName0 = String.valueOf(System.currentTimeMillis());
                        String newFileName3 = newFileName0 + "." + fileExt;
                        String dirName = accountBean.getUserName();
                        String timeSeq = df.format(new Date());
                        String path = dirName + File.separator + timeSeq + File.separator + newFileName3;
                        String savePath = rootPath + File.separator + dirName + File.separator + timeSeq + File.separator;
                        this.checkStorePath(savePath);
                        File uploadedFile3 = new File(savePath, newFileName3);
                        file.transferTo(uploadedFile3);
                        String thumbMark = request.getParameter(Constants.THUMB_MARK_KEY);
                        if (Constants.THUMB_MARK_VAL.equals(thumbMark)) {
                            List<ImageDocThumbBean> thumbBeans = accountBean.getThumbConfig();
                            if (thumbBeans != null) {
                                Iterator thumbBean = thumbBeans.iterator();

                                while(thumbBean.hasNext()) {
                                    ImageDocThumbBean thumb = (ImageDocThumbBean)thumbBean.next();
                                    File uploadedFile1 = new File(savePath, newFileName0 + thumb.getSuffix() + "." + fileExt);
                                    ImageUtils.ratioZoom2(uploadedFile3, uploadedFile1, thumb.getRatio());
                                }
                            }
                        }

                        result.setFilePath(path);
                        result.buildSuccess();
                    } catch (Exception var22) {
                        result.buildFailed();
                    }
                } else {
                    result.buildFailed();
                }
            }
            result.buildFailed();
            return result;
        }
    }

}
