package org.fortune.commons.core.http;

import org.apache.http.Consts;
import org.apache.http.config.*;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.nio.charset.CodingErrorAction;

/**
 * @author landyl
 * @create 10:10 08/23/2019
 */
class HttpClientManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientManager.class);

    private static PoolingHttpClientConnectionManager connManager = null;

    static {
        try {
            // setup a Trust Strategy that allows all certificates.
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();
            // here's the special part:
            //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
            //      -- and create a Registry, to register it.
            //
            RegistryBuilder<ConnectionSocketFactory> builder = RegistryBuilder.create();
            builder.register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext));

            Registry<ConnectionSocketFactory> socketFactoryRegistry = builder.build();
            // now, we create connection-manager using our Registry.
            //      -- allows multi-threaded use
            connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            connManager.setDefaultSocketConfig(socketConfig);
            MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000).build();
            ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).setMessageConstraints(messageConstraints).build();
            connManager.setDefaultConnectionConfig(connectionConfig);
            connManager.setMaxTotal(200);
            connManager.setDefaultMaxPerRoute(20);
        } catch (Exception ex) {
            LOGGER.error("occurs an unexpected exception: ",ex);
        }
    }

    public static PoolingHttpClientConnectionManager getConnManager() {
        return connManager;
    }
}
