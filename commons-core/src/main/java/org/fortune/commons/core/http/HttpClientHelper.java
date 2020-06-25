package org.fortune.commons.core.http;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.fortune.commons.core.util.DateUtil;
import org.fortune.commons.core.util.JsonUtil;
import org.fortune.commons.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author landyl
 * @create 9:54 08/23/2019
 */
public class HttpClientHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHelper.class);

    private CloseableHttpClient client;
    private List<NameValuePair> nameValuePostBodies;
    private List<Header> headers;
    private String mimeType;
    private Map<String, String> bodyParams;
    //Used to Multiple part form data
    private Map<String, ContentBody> contentBodies;


    HttpClientHelper(RequestConfig requestConfig, List<NameValuePair> nameValuePostBodies, List<Header> headers, String mimeType, Map<String, String> bodyParams, Map<String, ContentBody> contentBodies, CookieStore cookieStore) {
        PoolingHttpClientConnectionManager connectionManager = HttpClientManager.getConnManager();
        HttpClientBuilder httpClientBuilder;
        if(requestConfig != null) {
            httpClientBuilder = HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager);
        } else {
            httpClientBuilder = HttpClients.custom().setConnectionManager(connectionManager);
        }

        if(CollectionUtils.isNotEmpty(cookieStore.getCookies())) {
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }

        this.client = httpClientBuilder.build();
        this.nameValuePostBodies = nameValuePostBodies;
        this.headers = headers;
        this.mimeType = mimeType;
        this.bodyParams = bodyParams;
        this.contentBodies = contentBodies;
    }

    public HttpResponseContent doPost(String url) {
        try {
            return this.doPost(url, HttpConstants.ENCODING_UTF8);
        } catch (Exception ex) {
            LOGGER.error("occurs an unexpected exception,url:{}", url, ex);
            return null;
        }
    }

    public HttpResponseContent doPost(String url, String urlEncoding) throws HttpException, IOException {
        HttpRequestBase request = null;
        CloseableHttpResponse response = null;

        if(StringUtils.isBlank(urlEncoding)) urlEncoding = HttpConstants.ENCODING_UTF8;

        try {
            HttpPost httpPost = new HttpPost(url);
            //set bodies
            if(ContentType.APPLICATION_FORM_URLENCODED.getMimeType().equals(mimeType)) {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePostBodies, urlEncoding));
            } else if(ContentType.APPLICATION_JSON.getMimeType().equals(mimeType)){
                String bodyParamsJson = JsonUtil.toJSONString(bodyParams);
                StringEntity stringEntity = new StringEntity(bodyParamsJson, ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), urlEncoding));
                httpPost.setEntity(stringEntity);
            } else if(ContentType.MULTIPART_FORM_DATA.getMimeType().equals(mimeType)){
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                //file content body
                Iterator it = this.contentBodies.entrySet().iterator();

                while(it.hasNext()) {
                    Map.Entry<String, ContentBody> item = (Map.Entry)it.next();
                    entityBuilder.addPart(item.getKey(), item.getValue());
                }

                //another body parameters
                Iterator<NameValuePair> nameValuePairIterator = this.nameValuePostBodies.iterator();
                while(nameValuePairIterator.hasNext()) {
                    NameValuePair nameValuePair = nameValuePairIterator.next();
                    entityBuilder.addPart(nameValuePair.getName(), new StringBody(nameValuePair.getValue(), ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), urlEncoding)));
                }
                entityBuilder.setCharset(CharsetUtils.get(urlEncoding));
                httpPost.setEntity(entityBuilder.build());
            } else {
                throw new HttpException("Currently, we do not support the mine type equals to {} " + mimeType);
            }

            //set headers
            for (Header header : headers) {
                httpPost.setHeader(header);
            }
            request = httpPost;
            response = this.client.execute(httpPost);
            HttpResponseContent responseContent = this.buildResponseContent(response);
            return responseContent;
        } finally {
            this.close(request, response);
        }
    }

    public CloseableHttpClient getClient() {
        return this.client;
    }

    private HttpResponseContent buildResponseContent(CloseableHttpResponse response) throws HttpException, IOException {
        HttpResponseContent responseContent = new HttpResponseContent();
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        try {
            Header enHeader = entity.getContentEncoding();
            if (enHeader != null) {
                String encoding = enHeader.getValue().toLowerCase();
                responseContent.setEncoding(encoding);
            }

            String contentType = this.getResponseContentType(entity);
            int statusCode = statusLine.getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), HttpConstants.ENCODING_UTF8);
                throw new HttpException("Invalid status code: " + statusCode + ", result: " + result);
            }

            responseContent.setStatusCode(statusCode);
            responseContent.setContentType(contentType);
            responseContent.setContentTypeString(this.getResponseContentTypeString(entity));
            responseContent.setContentBytes(EntityUtils.toByteArray(entity));
        } finally {
            if (entity != null) {
                entity.getContent().close();
            }
        }
        return responseContent;
    }


    private String getResponseContentType(HttpEntity method) {
        Header contentType = method.getContentType();
        if (contentType == null) {
            return null;
        } else {
            String ret = null;

            try {
                HeaderElement[] hes = contentType.getElements();
                if (hes != null && hes.length > 0) {
                    ret = hes[0].getName();
                }
            } catch (Exception var5) {
            }

            return ret;
        }
    }

    private String getResponseContentTypeString(HttpEntity method) {
        Header contentType = method.getContentType();
        return contentType == null ? null : contentType.getValue();
    }

    private void close(HttpRequestBase request, CloseableHttpResponse response) throws IOException {
        if (request != null) {
            request.releaseConnection();
        }
        if (response != null) {
            response.close();
        }
    }

    public static class Builder {
        private List<NameValuePair> nameValuePostBodies = new LinkedList();
        private List<Header> headers = new LinkedList();
        private RequestConfig requestConfig;

        private Integer socketTimeout = 60000;
        private Integer connectTimeout = 60000;
        private Integer connectionRequestTimeout = 60000;

        private String mimeType;
        private Map<String, String> bodyParamsCopy;
        //Used to Multiple part form data
        private Map<String, ContentBody> contentBodies;

        private CookieStore cookieStore = new BasicCookieStore();

        private static final int cookieExpire = 7 * 24 * 60 * 60; //默认cookie失效时间

        public Builder() {
            contentBodies = new LinkedHashMap<>();
        }

        public Builder(Integer socketTimeout, Integer connectTimeout, Integer connectionRequestTimeout) {
            this();
            this.socketTimeout = socketTimeout;
            this.connectTimeout = connectTimeout;
            this.connectionRequestTimeout = connectionRequestTimeout;
        }

        public Builder setBodyParams(Map<String, String> bodyParams) {
            bodyParamsCopy = bodyParams;
            if (bodyParams != null && !bodyParams.isEmpty()) {
                for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
                    nameValuePostBodies.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            return this;
        }

        public Builder setBodyParams(String name, String value) {
            BasicNameValuePair nvp = new BasicNameValuePair(name, value);
            this.nameValuePostBodies.add(nvp);
            if(this.bodyParamsCopy == null) {
                this.bodyParamsCopy = new HashMap<>();
            }
            this.bodyParamsCopy.put(name, value);
            return this;
        }

        public Builder setHeaderParams(Map<String, String> headerParams) {
            if(headerParams.containsKey(HttpConstants.CONTENT_TYPE)) {
                mimeType = headerParams.get(HttpConstants.CONTENT_TYPE);
            }
            if (headerParams != null && !headerParams.isEmpty()) {
                for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                    //https://github.com/landy8530/fortune-commons/issues/44
                    if(!isMultiplePart(mimeType)) {
                        headers.add(new BasicHeader(entry.getKey(), entry.getValue()));
                    }
                }
            }
            return this;
        }

        public Builder setMimeType(String mimeType) {
            this.mimeType = mimeType;
            //https://github.com/landy8530/fortune-commons/issues/44
            if(!isMultiplePart(mimeType)) {
                headers.add(new BasicHeader(HttpConstants.CONTENT_TYPE, mimeType));
            }
            return this;
        }

        public Builder setRequestConfig(RequestConfig requestConfig) {
            this.requestConfig = requestConfig;
            return this;
        }

        public Builder setRequestConfig(Integer socketTimeout, Integer connectTimeout, Integer connectionRequestTimeout) {
            this.requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
            return this;
        }

        public Builder addContent(String fileDataKey, File file) {
            FileBody fileBody = new FileBody(file);
            this.contentBodies.put(fileDataKey, fileBody);
            return this;
        }

        public Builder addContent(String fileDataKey, byte[] content) {
            ByteArrayBody byteArrayBody = new ByteArrayBody(content, fileDataKey);
            this.contentBodies.put(fileDataKey, byteArrayBody);
            return this;
        }

        public Builder addContentBodies(Map<String, ContentBody> contentBodies) {
            if(contentBodies != null) {
                this.contentBodies.putAll(contentBodies);
            }
            return this;
        }

        public Builder setCookieStore(String cookieName, String cookieValue, String domain, String path, int expire) {
            if(StringUtil.hasText(cookieName) && StringUtil.hasText(cookieValue)) {
                BasicClientCookie cookie = new BasicClientCookie(cookieName, cookieValue);
                cookie.setDomain(domain);
                cookie.setPath(path);
                cookie.setExpiryDate(DateUtil.plusSeconds(new Date(), expire));
                cookieStore.addCookie(cookie);
            }
            return this;
        }

        public Builder setCookieStore(String cookieName, String cookieValue, String domain) {
            return setCookieStore(cookieName, cookieValue, domain, "/", cookieExpire);
        }

        public Builder setCookieStore(String cookieName, String cookieValue) {
            return setCookieStore(cookieName, cookieValue, null);
        }

        private boolean isMultiplePart(String mimeType) {
            return ContentType.MULTIPART_FORM_DATA.getMimeType().equals(mimeType);
        }

        public HttpClientHelper build() {
            if(this.requestConfig == null) {
                this.setRequestConfig(socketTimeout, connectTimeout, connectionRequestTimeout);
            }
            if(StringUtils.isBlank(mimeType)) mimeType = ContentType.APPLICATION_JSON.getMimeType();
            return new HttpClientHelper(requestConfig, nameValuePostBodies, headers, mimeType, bodyParamsCopy , contentBodies, cookieStore);
        }
    }
}
