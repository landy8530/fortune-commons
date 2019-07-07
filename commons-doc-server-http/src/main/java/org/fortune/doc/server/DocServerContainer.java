package org.fortune.doc.server;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fortune.commons.core.help.AbstractApplicationContextHelper;
import org.fortune.doc.common.domain.account.Account;
import org.fortune.doc.common.domain.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: landy
 * @date: 2019/6/2 22:03
 * @description: 文件服务器基本信息容器
 */
public class DocServerContainer extends AbstractApplicationContextHelper {
    public static final String BEAN_NAME_DOC_SERVER_CONTAINER= "docServerContainer";
    private static Log log = LogFactory.getLog(DocServerContainer.class);
    /**
     * 文件服务器上传鉴权账户列表
     */
    private List<Account> accounts = new ArrayList<Account>();
    /**
     * 文件上传的根目录
     */
    private String fileBaseDirectory;
    private int port;
    private String imagesBasePath;
    private String attachmentBasePath;
    private static Map<String, Account> accountMap = new HashMap<String, Account>();

    public static DocServerContainer getInstance() {
        return getBean(BEAN_NAME_DOC_SERVER_CONTAINER,DocServerContainer.class);
    }

    @Override
    public void init() {
        for (Account item : this.accounts) {
            if ((StringUtils.isBlank(item.getUserName()))
                    || (StringUtils.isBlank(item.getPassword()))
                    || (StringUtils.isBlank(item.getRootPath()))) {
                log.error("账户配置出现错误，请检查，" + item);
                new Exception().printStackTrace();
            }
            if (!accountMap.containsKey(item.getUserName())) {
                log.info("加入账户：" + item.getUserName());
                accountMap.put(item.getUserName(), item);
            } else
                log.error("账户出现重复配置：" + item);
        }

        log.info("加入默认账户：" + Constants.DEFAULT_ACCOUNT);
        accountMap.put(Constants.DEFAULT_ACCOUNT.getUserName(),
                Constants.DEFAULT_ACCOUNT);
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Account getAccount(String userName) {
        Account account = accountMap.get(userName);
        if (account == null) {
            log.error("不存在账户UserName=" + userName + ",返回默认账户");
            account = Constants.DEFAULT_ACCOUNT;
        }
        return account;
    }

    public String getFileBaseDirectory() {
        return fileBaseDirectory;
    }

    public void setFileBaseDirectory(String fileBaseDirectory) {
        this.fileBaseDirectory = fileBaseDirectory;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getImagesBasePath() {
        return imagesBasePath;
    }

    public void setImagesBasePath(String imagesBasePath) {
        this.imagesBasePath = imagesBasePath;
    }

    public String getAttachmentBasePath() {
        return attachmentBasePath;
    }

    public void setAttachmentBasePath(String attachmentBasePath) {
        this.attachmentBasePath = attachmentBasePath;
    }
}
