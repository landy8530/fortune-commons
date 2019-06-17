package org.fortune.doc.server.handler.image;

import org.apache.commons.lang3.StringUtils;
import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.account.DocAccountBean;
import org.fortune.doc.common.domain.account.ImageDocThumbBean;
import org.fortune.doc.common.domain.result.ImageDocResult;
import org.fortune.doc.server.handler.DocServerHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * @author: landy
 * @date: 2019/6/16 12:55
 * @description:
 */
@Component
public class DeleteImageServerHandler extends DocServerHandler {

    public ImageDocResult doDelete(HttpServletRequest request) {
        ImageDocResult result = new ImageDocResult();
        DocAccountBean accountBean = super.getAccount(request);
        if (accountBean == null) {
            result.buildFailed();
            result.buildCustomMsg("账号信息不对，请重新确认");
            return result;
        } else {
            String filePath = request.getParameter(Constants.FILE_PATH_KEY);
            if (StringUtils.isEmpty(filePath)) {
                result.buildCustomMsg("删除的文件路径为空");
                result.buildFailed();
                return result;
            } else {
                try {
                    String rootPath = super.getImageRootPath();
                    this.checkRootPath(rootPath);
                    String fileExt = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
                    String srcFilePathName = filePath.substring(0, filePath.lastIndexOf("."));
                    String realPath = this.getRealPath(filePath);
                    File oldFile = new File(realPath);
                    if (!oldFile.exists() || !oldFile.isFile()) {
                        result.buildCustomMsg("删除的文件不存在");
                        result.buildFailed();
                        return result;
                    }

                    oldFile.delete();
                    List<ImageDocThumbBean> thumbBeans = accountBean.getThumbConfig();
                    if (thumbBeans != null) {
                        Iterator thumbBean = thumbBeans.iterator();

                        while(thumbBean.hasNext()) {
                            ImageDocThumbBean thumb = (ImageDocThumbBean)thumbBean.next();
                            String thumbFilePath = this.getRealPath(srcFilePathName + thumb.getSuffix() + "." + fileExt);
                            File thumbFile = new File(thumbFilePath);
                            if (thumbFile.exists()) {
                                thumbFile.delete();
                            }
                        }
                    }

                    result.setFilePath("");
                    result.buildSuccess();
                } catch (Exception ex) {
                    result.buildFailed();
                }

                result.buildFailed();
                return result;
            }
        }
    }

}
