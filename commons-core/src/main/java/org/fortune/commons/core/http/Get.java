package org.fortune.commons.core.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: landy
 * @date: 2019/11/20 22:06
 * @description:
 */
public class Get extends HttpRequest implements FormHttpRequest {
    List<NameValuePair> parameters = new ArrayList();

    @Override
    public void addParameter(String key, String value) {
        this.parameters.add(new BasicNameValuePair(key, value));
    }


    @Override
    HttpRequestBase createRequest(String url) throws IOException {
        String completeURL = this.appendParams(url);
        return new HttpGet(completeURL);
    }

    @Override
    protected void logRequestParams() {
        logRequestParams(this.parameters);
    }

    String appendParams(String rootURL) {
        if (this.parameters != null) {
            String queryChar = rootURL.contains("?") ? "&" : "?";
            return rootURL + queryChar + URLEncodedUtils.format(this.parameters, "ISO-8859-1");
        } else {
            return rootURL;
        }
    }
}
