package org.fortune.commons.crawler.util;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import org.fortune.commons.crawler.exception.WebSiteErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HtmlUnitClient {
    private WebClient client;
    private Logger logger = LoggerFactory.getLogger(HtmlUnitClient.class);
    WebRequest request;
    private Page page;

    public HtmlUnitClient() {
        client = new WebClient(BrowserVersion.CHROME);
        initOptions(client.getOptions());
    }

    public HtmlUnitClient(BrowserVersion version) {
        client = new WebClient(version);
        initOptions(client.getOptions());
    }

    private void initOptions(WebClientOptions options) {
        options.setJavaScriptEnabled(false);
        options.setCssEnabled(false);
        options.setThrowExceptionOnScriptError(false);
        options.setRedirectEnabled(true);
        options.setTimeout(3600 * 1000);
        options.setThrowExceptionOnFailingStatusCode(true);
        options.setPopupBlockerEnabled(false);
    }

    public HtmlUnitClient post(String url, String requestBody) {
        initRequest(url, HttpMethod.POST, requestBody);
        return this;
    }

    public HtmlUnitClient post(String url) {
        initRequest(url, HttpMethod.POST);
        return this;
    }

    private void initRequest(String url, HttpMethod hm) {
        try {
            request = new WebRequest(new URL(url), hm);
            request.setCharset("UTF-8");
        } catch (MalformedURLException e) {
            logger.error("URL Error : \n" + e.getMessage(), e);
            throw new WebSiteErrorException("URL Error : \n" + e.getMessage(),
                    e);
        }
    }

    private void initRequest(String url, HttpMethod hm, String requestBody) {
        try {
            request = new WebRequest(new URL(url), hm);
            request.setRequestBody(requestBody);
            request.setCharset("UTF-8");
        } catch (MalformedURLException e) {
            logger.error("URL Error : \n" + e.getMessage(), e);
            throw new WebSiteErrorException("URL Error : \n" + e.getMessage(),
                    e);
        }
    }

    public Set<com.gargoylesoftware.htmlunit.util.Cookie> getCookies() {
        return client.getCookieManager().getCookies();
    }

    public HtmlUnitClient url(String url) {
        initRequest(url, HttpMethod.GET);
        return this;
    }

    public <P extends Page> P submit() {
        try {
            page = client.getPage(request);
            return (P) page;
        } catch (IOException e) {
            logger.error("Parse Error : \n" + e.getMessage(), e);
            throw new WebSiteErrorException(
                    "Parse Error : \n" + e.getMessage(), e);
        }
    }

    public HtmlUnitClient setCredentials(String username, String password) {
        DefaultCredentialsProvider provider = new DefaultCredentialsProvider();
        provider.addCredentials(username, password);
        client.setCredentialsProvider(provider);
        return this;
    }

    public HtmlUnitClient addHeader(String name, String value) {
        request.getAdditionalHeaders().put(name, value);
        return this;
    }

    public HtmlUnitClient addParam(String name, String value) {
        if (HttpMethod.POST.equals(request.getHttpMethod())) {
            if (request.getRequestParameters().isEmpty()) {
                request.setRequestParameters(new ArrayList<NameValuePair>());
            }
            request.getRequestParameters().add(new NameValuePair(name, value));
        }
        return this;
    }

    public HtmlUnitClient setParamValue(String name, String value) {
        addParam(name, value);
        return this;
    }

    public HtmlUnitClient setRequestParameters(List<NameValuePair> parametersList) {
        if (HttpMethod.POST.equals(request.getHttpMethod())) {
            request.setRequestParameters(parametersList);
        }
        return this;
    }

    public String getResponseText() {
        if (page instanceof HtmlPage)
            return ((HtmlPage) page).asXml();
        else {
            return ((TextPage) page).getContent();
        }
    }

    public WebClient getClient() {
        return client;
    }



}