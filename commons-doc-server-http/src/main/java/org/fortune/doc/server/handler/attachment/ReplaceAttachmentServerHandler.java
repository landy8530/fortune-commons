package org.fortune.doc.server.handler.attachment;

import org.apache.commons.lang3.StringUtils;
import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.account.DocAccountBean;
import org.fortune.doc.common.domain.result.AttachDocResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author: landy
 * @date: 2019/6/24 23:01
 * @description:
 */
@Component
public class ReplaceAttachmentServerHandler extends AttachmentServerHandler {

    public AttachDocResult doReplace(HttpServletRequest request) {
        AttachDocResult result = new AttachDocResult();
        DocAccountBean accountBean = super.getAccount(request);
        if (accountBean == null) {
            result.buildFailed();
            result.buildCustomMsg("账号信息不对，请重新确认");
            return result;
        } else {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest mreqeust = (MultipartHttpServletRequest)request;
                String filePath = mreqeust.getParameter(Constants.FILE_PATH_KEY);
                if (StringUtils.isEmpty(filePath)) {
                    result.buildCustomMsg("替换的文件路径为空");
                    result.buildFailed();
                    return result;
                }

                MultipartFile file = mreqeust.getFile(Constants.FILE_DATA_KEY);
                if (!file.isEmpty()) {
                    try {
                        String rootPath = super.getAttachmentRootPath();
                        this.checkRootPath(rootPath);
                        String realPath = this.getRealPath(filePath);
                        File oldFile = new File(realPath);
                        if (!oldFile.exists() || !oldFile.isFile()) {
                            result.buildCustomMsg("替换的文件不存在");
                            result.buildFailed();
                            return result;
                        }

                        oldFile.delete();
                        file.transferTo(oldFile);
                        result.setFilePath("");
                        result.buildSuccess();
                    } catch (Exception var10) {
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
