package org.fortune.doc.server.handler;

import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.account.DocAccountBean;
import org.fortune.doc.server.DocServerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author: landy
 * @date: 2019/6/16 11:35
 * @description:
 */
public abstract class DocServerHandler {
    protected Logger LOGGER = LoggerFactory.getLogger(DocServerHandler.class);

    private DocAccountBean account;

    public DocAccountBean getAccount(HttpServletRequest request) {
        String account = request.getParameter(Constants.USER_NAME_KEY);
        String password = request.getParameter(Constants.PWD_KEY);
        DocAccountBean accountBean = (DocAccountBean)DocServerContainer.getInstance().getAccount(account);
        this.account = (accountBean != null && accountBean.auth(password) ? accountBean : null);
        return accountBean != null && accountBean.auth(password) ? accountBean : null;
    }

    protected String getRootPath() {
        return this.account.getRootPath() ;
    }

    protected void checkRootPath(String rootPath) {
        File uploadDir = new File(rootPath);
        Assert.isTrue(uploadDir.isDirectory(), "上传的路径非目录");
        Assert.isTrue(uploadDir.canWrite(), "上传的路径不可写");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    protected void checkStorePath(String storePath) {
        File saveDirFile = new File(storePath);
        if (!saveDirFile.exists()) {
            saveDirFile.mkdirs();
        }
    }

    protected abstract String getRealPath(String filePath) ;

}
