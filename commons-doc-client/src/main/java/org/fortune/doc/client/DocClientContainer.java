package org.fortune.doc.client;

import org.fortune.commons.core.help.ApplicationContextHelper;

/**
 * @author: landy
 * @date: 2019/5/30 23:39
 * @description:
 */
public class DocClientContainer {
    public static final String BEAN_NAME_DOC_CLIENT_CONTAINER = "docClientContainer";

    private static String userName ;
    private static String password ;
    private static String host ;
    private static int port ;

    public static DocClientContainer getInstance() {
        return ApplicationContextHelper.accessApplicationContext().getBean(BEAN_NAME_DOC_CLIENT_CONTAINER,DocClientContainer.class);
    }

    public DocClientContainer(String userName, String password, String host, int port) {
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
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
