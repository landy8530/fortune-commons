package org.fortune.commons.core.http;


import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.fortune.commons.core.exception.HttpException;
import org.fortune.commons.core.util.ClasspathResource;
import org.fortune.commons.core.util.StopWatch;
import org.fortune.commons.core.util.TimeLength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyStore;

/**
 * @author: landy
 * @date: 2019/11/20 22:39
 * @description:
 */
public class HttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

    public static final TimeLength DEFAULT_TIME_OUT = TimeLength.minutes(2L);
    public static final TimeLength NO_TIME_OUT = TimeLength.ZERO;

    private static final String TLS = "TLS";
    private static final String[] ENABLED_SSL_PROTOCOLS = null;
    private static final String[] ENABLED_SSL_CIPHERS = null;
    private CloseableHttpClient httpClient;
    private RequestConfig defaultRequestConfig;
    private String cookieSpecs = "default";
    private TimeLength timeOut;
    private String basicAuthUser;
    private String basicAuthPassword;
    private boolean handleRedirect;
    private ClasspathResource clientKeyStoreResource;
    private String clientKeyStorePassword;
    boolean acceptSelfSignedCert;
    boolean validateStatusCode;
    private String defaultURL;

    public HttpClient() {
        this.timeOut = DEFAULT_TIME_OUT;
        this.acceptSelfSignedCert = true;
        this.validateStatusCode = true;
        this.defaultRequestConfig = RequestConfig.custom().setCookieSpec(this.cookieSpecs).setSocketTimeout((int)this.timeOut.toMilliseconds()).setConnectTimeout((int)this.timeOut.toMilliseconds()).setConnectionRequestTimeout((int)this.timeOut.toMilliseconds()).build();
    }

    public HttpResponse execute(HttpRequest request) {
        StopWatch watch = new StopWatch();

        HttpResponse httpResponse;
        try {
            HttpClientContext context = HttpClientContext.create();
            org.apache.http.HttpResponse response = this.executeMethod(request, context);
            String responseText = this.readResponseText(response);
            HttpStatusCode statusCode = new HttpStatusCode(response.getStatusLine().getStatusCode());
            this.validateStatusCode(statusCode);
            HttpHeaders headers = HttpHeaders.createResponseHeaders(response);
            httpResponse = new HttpResponse(statusCode, responseText, context.getCookieStore(), headers);
        } catch (IOException ex) {
            throw new HttpException(ex);
        } finally {
            LOGGER.info("execute finished, elapsedTime={} (ms)", watch.elapsedTime());
        }

        return httpResponse;
    }

    public HttpBinaryResponse download(HttpRequest request) {
        StopWatch watch = new StopWatch();

        HttpBinaryResponse httpBinaryResponse;
        try {
            HttpClientContext context = HttpClientContext.create();
            org.apache.http.HttpResponse response = this.executeMethod(request, context);
            byte[] binaryResponseContent = this.readResponseBytes(response);
            HttpStatusCode statusCode = new HttpStatusCode(response.getStatusLine().getStatusCode());
            this.validateStatusCode(statusCode);
            HttpHeaders headers = HttpHeaders.createResponseHeaders(response);
            httpBinaryResponse = new HttpBinaryResponse(statusCode, headers, binaryResponseContent, context.getCookieStore());
        } catch (IOException ex) {
            throw new HttpException(ex);
        } finally {
            LOGGER.info("execute finished, elapsedTime={} (ms)", watch.elapsedTime());
        }

        return httpBinaryResponse;
    }


    private org.apache.http.HttpResponse executeMethod(HttpRequest request, HttpClientContext context) throws IOException {
        HttpRequestBase httpRequest = request.createHttpRequest(this.defaultURL);
        LOGGER.info("send request, url=" + httpRequest.getURI() + ", method=" + httpRequest.getMethod());
        request.logRequest();
        org.apache.http.HttpResponse response = this.getOrCreateHttpClient().execute(httpRequest, context);
        LOGGER.info("received response, statusCode=" + response.getStatusLine().getStatusCode());
        return response;
    }

    private String readResponseText(org.apache.http.HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity, Consts.UTF_8);
    }

    private byte[] readResponseBytes(org.apache.http.HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        byte[] responseContent = EntityUtils.toByteArray(entity);
        LOGGER.info("lengthOfResponseContent=" + responseContent.length);
        return responseContent;
    }

    void validateStatusCode(HttpStatusCode statusCode) {
        if (this.validateStatusCode) {
            if (!statusCode.isSuccess()) {
                if (!statusCode.isRedirect()) {
                    throw new HttpException("invalid response status code, statusCode=" + statusCode);
                }
            }
        }
    }

    synchronized CloseableHttpClient getOrCreateHttpClient() {
        if (this.httpClient != null) {
            return this.httpClient;
        } else {
            HttpClientBuilder httpClientBuilder = HttpClients.custom().setDefaultRequestConfig(this.defaultRequestConfig);

            this.configureBasicCredentials(httpClientBuilder);
            this.configureRedirectHandling(httpClientBuilder);
            this.configureSecureConnections(httpClientBuilder);
            this.httpClient = httpClientBuilder.build();
            return this.httpClient;
        }
    }

    private void configureBasicCredentials(HttpClientBuilder httpClientBuilder) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        if (this.basicAuthUser != null) {
            credentialsProvider.setCredentials(new AuthScope(AuthScope.ANY), new UsernamePasswordCredentials(this.basicAuthUser, this.basicAuthPassword));
        }
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
    }

    private void configureRedirectHandling(HttpClientBuilder httpClientBuilder) {
        if (this.handleRedirect) {
            httpClientBuilder.disableRedirectHandling();
        }
    }

    private void configureSecureConnections(HttpClientBuilder httpClientBuilder) {
        try {
            SSLContextBuilder sslContextBuilder = SSLContexts.custom().useProtocol(TLS);
            if (this.clientKeyStoreResource != null) {
                KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                clientKeyStore.load(this.clientKeyStoreResource.getInputStream(), this.clientKeyStorePassword.toCharArray());
                sslContextBuilder.loadKeyMaterial(clientKeyStore, this.clientKeyStorePassword.toCharArray());
            }

            if (this.acceptSelfSignedCert) {
                sslContextBuilder.loadTrustMaterial(CustomTrustSelfSignedStrategy.INSTANCE);
            }

            SSLContext sslContext = sslContextBuilder.build();
            HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.getDefaultHostnameVerifier();
            if (this.acceptSelfSignedCert) {
                hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            }

            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, ENABLED_SSL_PROTOCOLS, ENABLED_SSL_CIPHERS, hostnameVerifier);

            // here's the special part:
            //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
            //      -- and create a Registry, to register it.
            //
            RegistryBuilder<ConnectionSocketFactory> builder = RegistryBuilder.create();
            builder.register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", sslSocketFactory);

            Registry<ConnectionSocketFactory> socketFactoryRegistry = builder.build();

            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            httpClientBuilder.setConnectionManager(connectionManager);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new HttpException(ex);
        }
    }

    public void setCookiePolicy(String cookiePolicy) {
        if ("netscape".equalsIgnoreCase(cookiePolicy)) {
            this.cookieSpecs = "netscape";
        } else if ("standard".equalsIgnoreCase(cookiePolicy)) {
            this.cookieSpecs = "standard";
        } else if ("standard-strict".equalsIgnoreCase(cookiePolicy)) {
            this.cookieSpecs = "standard-strict";
        } else if ("ignoreCookies".equalsIgnoreCase(cookiePolicy)) {
            this.cookieSpecs = "ignoreCookies";
        } else {
            this.cookieSpecs = "default";
        }
    }

    public void setTimeOut(TimeLength timeOut) {
        this.timeOut = timeOut;
    }

    public void setBasicCredentials(String basicAuthUser, String basicAuthPassword) {
        this.basicAuthUser = basicAuthUser;
        this.basicAuthPassword = basicAuthPassword;
    }

    public void setHandleRedirect(boolean handleRedirect) {
        this.handleRedirect = handleRedirect;
    }

    public void setClientKeyStore(ClasspathResource clientKeyStoreResource, String clientKeyStorePassword) {
        this.clientKeyStoreResource = clientKeyStoreResource;
        this.clientKeyStorePassword = clientKeyStorePassword;
    }

    public void setAcceptSelfSignedCert(boolean acceptSelfSignedCert) {
        this.acceptSelfSignedCert = acceptSelfSignedCert;
    }

    public void setValidateStatusCode(boolean validateStatusCode) {
        this.validateStatusCode = validateStatusCode;
    }

    public void setDefaultURL(String defaultURL) {
        this.defaultURL = defaultURL;
    }
}
