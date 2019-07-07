package org.fortune.doc.client;

import org.fortune.commons.core.help.ApplicationContextHelper;
import org.fortune.doc.common.domain.Constants;

/**
 * @author: landy
 * @date: 2019/5/30 23:39
 * @description:
 */
public class DocClientContainer {
    public static final String BEAN_NAME_DOC_CLIENT_CONTAINER = "docClientContainer";

    private String userName ;
    private String password ;
    private String host ;
    private String docServerDomainName ;
    private int port ;
    private int accessPort;

    public static DocClientContainer getInstance() {
        return ApplicationContextHelper.accessApplicationContext().getBean(BEAN_NAME_DOC_CLIENT_CONTAINER,DocClientContainer.class);
    }

    public DocClientContainer(String userName, String password, String host, int port, String docServerDomainName, int accessPort) {
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
        this.docServerDomainName = docServerDomainName;
        this.accessPort = accessPort;
    }

    public String getDocServerUrl() {
        //${host}:${port}/${domainName}/
        return "http://" + host + ":" + accessPort + Constants.BACKSLASH + docServerDomainName + Constants.BACKSLASH;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}
