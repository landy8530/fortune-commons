package org.fortune.commons.core.http;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author landyl
 * @create 11:11 08/23/2019
 */
public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    public static HttpResponseContent doPost(String url, Map<String, String> bodyParams, Map<String, String> headerParams, Map<String, ContentBody> contentBodies) {
        return doPost(url, bodyParams, headerParams, contentBodies, null, null);
    }

    public static HttpResponseContent doPost(String url, Map<String, String> bodyParams, Map<String, String> headerParams, Map<String, ContentBody> contentBodies, String cookieName, String cookieValue) {
        return doPost(url, bodyParams, headerParams, contentBodies, cookieName, cookieValue, null);
    }

    public static HttpResponseContent doPost(String url, Map<String, String> bodyParams, Map<String, String> headerParams, Map<String, ContentBody> contentBodies, String cookieName, String cookieValue, String domain) {
        LOGGER.info("Access URL according HttpClient: {}", url);
        HttpClientHelper.Builder builder = create();
        if(headerParams == null) {
            headerParams = new HashMap<>();
            headerParams.put(HttpConstants.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        }
        HttpClientHelper helper = builder.setBodyParams(bodyParams).setHeaderParams(headerParams).addContentBodies(contentBodies).setCookieStore(cookieName, cookieValue, domain).build();
        return helper.doPost(url);
    }

    public static HttpResponseContent doPost(String url, Map<String, String> bodyParams) {
        return doPost(url, bodyParams, null);
    }

    public static HttpResponseContent doPost(String url, Map<String, String> bodyParams, Map<String, ContentBody> contentBodies) {
        return doPost(url, bodyParams, null, contentBodies);
    }

    private static HttpClientHelper.Builder create() {
        return new HttpClientHelper.Builder();
    }

}
