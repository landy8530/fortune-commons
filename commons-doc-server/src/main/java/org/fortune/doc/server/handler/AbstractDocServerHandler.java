package org.fortune.doc.server.handler;

import org.fortune.doc.common.domain.Account;

/**
 * @author: landy
 * @date: 2019/6/2 21:00
 * @description:
 */
public abstract class AbstractDocServerHandler implements DocServerProcessor{

    protected Account account;

    public AbstractDocServerHandler(Account account) {
        this.account = account;
    }
    /**
     * 服务器真实路径
     * @param savePath 数据库保存的路径
     * @return
     * @author Landy
     */
    protected String getRealPath(String savePath) {
        return this.account.getRootPath() + savePath;
    }
}
