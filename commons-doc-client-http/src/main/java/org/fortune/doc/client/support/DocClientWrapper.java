package org.fortune.doc.client.support;

import org.apache.http.HttpException;
import org.apache.http.entity.ContentType;
import org.fortune.commons.core.http.HttpClientHelper;
import org.fortune.commons.core.http.HttpResponseContent;
import org.fortune.doc.client.DocClientContainer;
import org.fortune.doc.client.result.DocResponseContent;
import org.fortune.doc.common.domain.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author: landy
 * @date: 2019/7/6 00:01
 * @description:
 */
public class DocClientWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocClientWrapper.class);

    private HttpClientHelper.Builder builder;

    private DocClientWrapper() {
    }

    public static DocClientWrapper newInstance() {
        DocClientWrapper docClientWrapper = new DocClientWrapper();
        docClientWrapper.builder = new HttpClientHelper.Builder();
        docClientWrapper.builder.setMimeType(ContentType.MULTIPART_FORM_DATA.getMimeType());
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

    public DocResponseContent doPost(String uri) {
        try {
            return this.doPost(uri, Constants.ENCODE_UTF_8);
        } catch (Exception ex) {
            LOGGER.error("occurs an unexpected exception,", ex);
            return null;
        }
    }

    public DocResponseContent doPost(String uri, String urlEncoding) throws HttpException, IOException {
        String url = this.buildUrl(uri);
        HttpClientHelper helper = this.builder.build();
        HttpResponseContent responseContent = helper.doPost(url, urlEncoding);
        DocResponseContent ret = getDocResponseContent(responseContent);
        return ret;
    }

    private DocResponseContent getDocResponseContent(HttpResponseContent responseContent) {
        DocResponseContent docResponseContent = new DocResponseContent();
        docResponseContent.setContentBytes(responseContent.getContentBytes());
        docResponseContent.setContentType(responseContent.getContentType());
        docResponseContent.setContentTypeString(responseContent.getContentTypeString());
        docResponseContent.setEncoding(responseContent.getEncoding());
        docResponseContent.setStatusCode(responseContent.getStatusCode());
        return docResponseContent;
    }

    public void addNV(String name, String value) {
        this.builder.setBodyParams(name, value);
    }

    public void addContent(String fileDataKey, File file) {
        this.builder.addContent(fileDataKey, file);
    }

    public void addContent(String fileDataKey, byte[] content) {
        this.builder.addContent(fileDataKey, content);
    }

}
