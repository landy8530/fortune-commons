package org.fortune.doc.client;

import java.util.ResourceBundle;

/**
 * @author: landy
 * @date: 2019/5/30 23:39
 * @description:
 */
public class DocClientContainer {

    private static ResourceBundle rb = null;

    static {
        rb = ResourceBundle.getBundle("file-config");
    }

    private static String userName = rb.getString("upload.userName");
    private static String password = rb.getString("upload.password");
    private static String host = rb.getString("upload.server.host");
    private static int port = Integer
            .parseInt(rb.getString("upload.server.port"));

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

}
