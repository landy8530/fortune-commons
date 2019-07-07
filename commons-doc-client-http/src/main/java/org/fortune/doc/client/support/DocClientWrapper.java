package org.fortune.doc.client.support;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.fortune.doc.client.DocClientContainer;
import org.fortune.doc.client.result.DocResponseContent;
import org.fortune.doc.common.domain.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author: landy
 * @date: 2019/7/6 00:01
 * @description:
 */
public class DocClientWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocClientWrapper.class);

    private Integer socketTimeout = 50;
    private Integer connectTimeout = 50;
    private Integer connectionRequestTimeout = 50;
    private CloseableHttpClient client;
    private RequestConfig requestConfig;
    private Map<String, ContentBody> contentBodies;
    private List<NameValuePair> nameValuePostBodies;
    static Set<Character> BEING_ESCAPED_CHARS = new HashSet();

    static {
        char[] signArray = new char[]{' ', '\\', '‘', ']', '!', '^', '#', '`', '$', '{', '%', '|', '}', '(', '+', ')', '<', '>', ';', '['};

        for(int i = 0; i < signArray.length; ++i) {
            BEING_ESCAPED_CHARS.add(new Character(signArray[i]));
        }

    }

    private DocClientWrapper() {
    }

    public static DocClientWrapper newInstance() {
        DocClientWrapper docClientWrapper = new DocClientWrapper();
        docClientWrapper.socketTimeout = DocClientContainer.socketTimeout;
        docClientWrapper.connectTimeout = DocClientContainer.connectTimeout;
        docClientWrapper.connectionRequestTimeout = DocClientContainer.connectionRequestTimeout;
        docClientWrapper.contentBodies = new LinkedHashMap();
        docClientWrapper.nameValuePostBodies = new LinkedList();
        docClientWrapper.client = HttpClients.custom().setConnectionManager(DocClientContainer.getConnManager()).build();
        docClientWrapper.requestConfig = RequestConfig.custom().setConnectionRequestTimeout(docClientWrapper.connectionRequestTimeout).setConnectTimeout(docClientWrapper.connectTimeout).setSocketTimeout(docClientWrapper.socketTimeout).build();
        return docClientWrapper;
    }

    private String buildUrl(String uri) {
        String serverUrl = DocClientContainer.getInstance().getDocServerUrl();
        if (serverUrl.endsWith(Constants.BACKSLASH)) {
            serverUrl = serverUrl + uri;
        } else {
            serverUrl = serverUrl + Constants.BACKSLASH + uri;
        }
        return serverUrl;
    }

    private void getResponseContent(HttpEntity entity, DocResponseContent ret) throws IOException {
        Header enHeader = entity.getContentEncoding();
        String contenttype;
        if (enHeader != null) {
            contenttype = enHeader.getValue().toLowerCase();
            ret.setEncoding(contenttype);
        }

        contenttype = this.getResponseContentType(entity);
        ret.setContentType(contenttype);
        ret.setContentTypeString(this.getResponseContentTypeString(entity));
        ret.setContentBytes(EntityUtils.toByteArray(entity));
    }

    public DocResponseContent doPost(String uri) {
        try {
            return this.doPost(uri, Constants.ENCODE_UTF_8);
        } catch (Exception ex) {
            LOGGER.error("occurs an unexpected exception,", ex);
            return null;
        }
    }

    public DocResponseContent doPost(String uri, String urlEncoding) throws HttpException, IOException {
        HttpEntity entity = null;
        HttpRequestBase request = null;
        CloseableHttpResponse response = null;

        try {
            String url = this.buildUrl(uri);
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            Iterator<NameValuePair> nameValuePairIterator = this.getNVBodies().iterator();

            while(nameValuePairIterator.hasNext()) {
                NameValuePair nameValuePair = nameValuePairIterator.next();
                entityBuilder.addPart(nameValuePair.getName(), new StringBody(nameValuePair.getValue(), ContentType.create("text/plain", urlEncoding)));
            }

            Iterator it = this.getContentBodies().entrySet().iterator();

            while(it.hasNext()) {
                Map.Entry<String, ContentBody> item = (Map.Entry)it.next();
                entityBuilder.addPart(item.getKey(), item.getValue());
            }

            entityBuilder.setCharset(CharsetUtils.get(urlEncoding));
            httpPost.setEntity(entityBuilder.build());
            request = httpPost;
            response = this.client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            entity = response.getEntity();
            DocResponseContent ret = new DocResponseContent();
            ret.setStatusCode(statusLine.getStatusCode());
            this.getResponseContent(entity, ret);
            DocResponseContent var12 = ret;
            return var12;
        } finally {
            this.close(entity, request, response);
        }
    }

    private void close(HttpEntity entity, HttpRequestBase request, CloseableHttpResponse response) throws IOException {
        if (request != null) {
            request.releaseConnection();
        }

        if (entity != null) {
            entity.getContent().close();
        }

        if (response != null) {
            response.close();
        }

    }

    public NameValuePair[] getNVBodyArray() {
        List<NameValuePair> list = this.getNVBodies();
        if (list != null && !list.isEmpty()) {
            NameValuePair[] nvps = new NameValuePair[list.size()];
            Iterator<NameValuePair> it = list.iterator();

            NameValuePair nvp;
            for(int index = 0; it.hasNext(); nvps[index++] = nvp) {
                nvp = it.next();
            }

            return nvps;
        } else {
            return null;
        }
    }

    public List<NameValuePair> getNVBodies() {
        return Collections.unmodifiableList(this.nameValuePostBodies);
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

    public static String encodeURL(String url, String encoding) {
        if (url == null) {
            return null;
        } else if (encoding == null) {
            return url;
        } else {
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < url.length(); ++i) {
                char c = url.charAt(i);
                if (c != '\n') {
                    if (!BEING_ESCAPED_CHARS.contains(new Character(c)) && c != '\r' && c <= '~') {
                        sb.append(c);
                    } else {
                        try {
                            sb.append(URLEncoder.encode(String.valueOf(c), encoding));
                        } catch (Exception var6) {
                            sb.append(c);
                        }
                    }
                }
            }

            return sb.toString().replaceAll("\\+", "%20");
        }
    }

    public void addNV(String name, String value) {
        BasicNameValuePair nvp = new BasicNameValuePair(name, value);
        this.nameValuePostBodies.add(nvp);
    }

    public void clearNVBodies() {
        this.nameValuePostBodies.clear();
    }

    public Map<String, ContentBody> getContentBodies() {
        return this.contentBodies;
    }

}
