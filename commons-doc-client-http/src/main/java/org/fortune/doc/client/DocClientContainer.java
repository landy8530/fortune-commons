package org.fortune.doc.client;

import org.apache.http.Consts;
import org.apache.http.config.*;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.fortune.commons.core.help.ApplicationContextHelper;
import org.fortune.doc.common.domain.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;

/**
 * @author: landy
 * @date: 2019/7/5 23:57
 * @description:
 */
public class DocClientContainer {
    public static final String BEAN_NAME_DOC_CLIENT_CONTAINER = "docClientContainer";

    private static final Logger LOGGER = LoggerFactory.getLogger(DocClientContainer.class);

    private static PoolingHttpClientConnectionManager connManager = null;
    private String docServerHost;
    private int docServerPort;
    private String docServerDomainName;
    private String userName;
    private String password;

    public static Integer socketTimeout = 50;
    public static Integer connectTimeout = 6000;
    public static Integer connectionRequestTimeout = 50;

    static {
        try {
            SSLContext sslContext = SSLContexts.custom().useTLS().build();
            sslContext.init((KeyManager[])null, new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, (SecureRandom)null);

            RegistryBuilder<ConnectionSocketFactory> builder = RegistryBuilder.create();
            builder.register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext));

            Registry<ConnectionSocketFactory> socketFactoryRegistry = builder.build();

            connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            connManager.setDefaultSocketConfig(socketConfig);
            MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000).build();
            ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).setMessageConstraints(messageConstraints).build();
            connManager.setDefaultConnectionConfig(connectionConfig);
            connManager.setMaxTotal(200);
            connManager.setDefaultMaxPerRoute(20);
        } catch (KeyManagementException ex) {
            LOGGER.error("occurs an unexpected exception: ",ex);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error("occurs an unexpected exception: ",ex);
        }
    }

    public static DocClientContainer getInstance() {
        return ApplicationContextHelper.accessApplicationContext().getBean(BEAN_NAME_DOC_CLIENT_CONTAINER,DocClientContainer.class);
    }

    public DocClientContainer(String docServerHost, int docServerPort, String docServerDomainName, String userName, String password) {
        this.docServerHost = docServerHost;
        this.docServerPort = docServerPort;
        this.docServerDomainName = docServerDomainName;
        this.userName = userName;
        this.password = password;
    }

    public static PoolingHttpClientConnectionManager getConnManager() {
        return connManager;
    }

    public String getDocServerUrl() {
        //${host}:${port}/${domainName}/
        return "http://" + docServerHost + ":" + docServerPort + Constants.BACKSLASH + docServerDomainName + Constants.BACKSLASH;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

}
