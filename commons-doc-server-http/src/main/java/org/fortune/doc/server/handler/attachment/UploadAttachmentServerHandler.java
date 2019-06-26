package org.fortune.doc.server.handler.attachment;

import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.account.DocAccountBean;
import org.fortune.doc.common.domain.result.AttachDocResult;
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
                String fileName = mreqeust.getParameter(Constants.FILE_PATH_KEY);
                MultipartFile file = mreqeust.getFile(Constants.FILE_DATA_KEY);
                if (!file.isEmpty()) {
                    try {
                        String rootPath = super.getAttachmentRootPath();
                        this.checkRootPath(rootPath);
                        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                        String newFileName3 = null;
                        String newFileName0 = String.valueOf(System.currentTimeMillis());
                        newFileName3 = newFileName0 + "." + fileExt;
                        String dirName = accountBean.getUserName();
                        String timeSeq = df.format(new Date());
                        String path = dirName + "/" + timeSeq + "/" + newFileName3;
                        String savePath = rootPath + "/" + dirName + "/" + timeSeq + "/";
                        this.checkStorePath(savePath);
                        File uploadedFile3 = new File(savePath, newFileName3);
                        file.transferTo(uploadedFile3);
                        result.setFilePath(path);
                        result.buildSuccess();
                    } catch (Exception var17) {
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
