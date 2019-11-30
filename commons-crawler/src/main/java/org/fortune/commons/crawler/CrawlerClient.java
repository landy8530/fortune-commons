package org.fortune.commons.crawler;


import org.fortune.commons.core.exception.HttpException;
import org.fortune.commons.core.http.*;
import org.fortune.commons.core.util.TimeLength;
import org.fortune.commons.crawler.builder.GetMethodBuilder;
import org.fortune.commons.crawler.builder.PostMethodBuilder;
import org.fortune.commons.crawler.exception.ExternalServiceException;
import org.fortune.commons.crawler.util.HTMLUtils;
import org.fortune.commons.crawler.util.URIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class CrawlerClient implements GetMethodBuilder, PostMethodBuilder {
    protected Logger LOGGER = LoggerFactory.getLogger(CrawlerClient.class);

    static final int DEFAULT_RETRY_COUNT = 2;

    HttpClient httpClient;

    BrowserCompatibleRedirectHandler redirectHandler;

    HttpRequest httpMethod;

    HttpResponse response;

    HttpBinaryResponse binaryResponse;

    URI lastVisitURI;

    int retryCount = DEFAULT_RETRY_COUNT;

    String failureFlag; //used to flag some special text, when such text occur, it means that the request fail.

    public CrawlerClient(HttpClient httpClient, BrowserCompatibleRedirectHandler redirectHandler) {
        this.httpClient = httpClient;
        this.redirectHandler = redirectHandler;
    }

    /**
     * POST 方式请求
     * @param url
     * @return
     */
    public PostMethodBuilder post(String url) {
        httpMethod = new FormPost();
        httpMethod.setUrl(mergeURI(url).toString());
        return this;
    }

    public PostMethodBuilder post(String url, String contentType, String body) {
        TextPost textPost = new TextPost();
        textPost.setBody(body);
        textPost.setContentType(contentType);
        httpMethod = textPost;
        httpMethod.setUrl(mergeURI(url).toString());
        return this;
    }

    /**
     * build Post request by form with number (start from 1)
     *
     * @param number the number of form (start from 1)
     * @return this
     */
    public PostMethodBuilder form(int number) {
        Document doc = HTMLUtils.parse(response.getResponseText());
        HTMLUtils.HTMLForm form = HTMLUtils.parseForm(doc, number - 1);
        httpMethod = new FormPost();
        httpMethod.setUrl(mergeURI(form.url).toString());
        for (Map.Entry<String, String> entry : form.params.entrySet()) {
            ((FormPost) httpMethod).addParameter(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * build Post request by form with name
     *
     * @param formName the form name
     * @return this
     */
    public PostMethodBuilder formWithName(String formName) {
        Document doc = HTMLUtils.parse(response.getResponseText());
        HTMLUtils.HTMLForm form = HTMLUtils.parseForm(doc, formName);
        httpMethod = new FormPost();
        httpMethod.setUrl(mergeURI(form.url).toString());
        for (Map.Entry<String, String> entry : form.params.entrySet()) {
            ((FormPost) httpMethod).addParameter(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * GET 方式请求
     * @param url
     * @return
     */
    public GetMethodBuilder url(String url) {
        httpMethod = new Get();
        httpMethod.setUrl(mergeURI(url).toString());
        return this;
    }

    URI mergeURI(String newPath) {
        return URIUtils.createURI(lastVisitURI, newPath);
    }

    public HttpResponse submit() {
        return submit(false);
    }

    public HttpResponse submit(boolean onceRedirect) {
        int count = 0;
        while (count <= retryCount) {
            try {
                lastVisitURI = URI.create(httpMethod.getUrl());
                response = httpClient.execute(httpMethod);
                if (onceRedirect) {
                    if ( response.getStatusCode().getStatusCode() == 302 || response.getStatusCode().getStatusCode() == 301) {
                        String localUrl = redirectHandler.getRedirectURL(response.getHeaders());
                        lastVisitURI = URI.create(localUrl);
                        response = httpClient.execute(httpMethod);
                    }
                } else {
                    response = redirectHandler.handleRedirect(this, response);
                }
                if (StringUtils.hasText(failureFlag) && response.getResponseText().contains(failureFlag)) {
                    throw new HttpException("The request is regarded as failure as it contains the failure flag: " + failureFlag);
                }
                break;
            } catch (HttpException e) {
                if (count == retryCount) {
                    throw new ExternalServiceException("Error when connect to Carrier web site: " + e.getMessage() + ". Retry " + retryCount + " times.", e);
                }
                sleepCurrentThread(5000);
                setTimeout(HttpClient.NO_TIME_OUT);
                count++;
                LOGGER.info("Retry " + count + " for " + e.getMessage());
            } catch (Exception e) {
                throw new ExternalServiceException("Error when connect to Carrier web site: " + e.getMessage(), e);
            }
        }
        revertRetryParameterToDefault();
        return response;
    }

    // revert these parameters related to retry to default value
    public void revertRetryParameterToDefault() {
        retryCount = DEFAULT_RETRY_COUNT;
        failureFlag = "";
        setTimeout(HttpClient.DEFAULT_TIME_OUT);
    }

    protected void sleepCurrentThread(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error("Exception occur when try to sleep current thread: " + e.getMessage());
        }
    }

    @Override
    public HttpBinaryResponse download() {
        lastVisitURI = URI.create(httpMethod.getUrl());
        binaryResponse = httpClient.download(httpMethod);
        return binaryResponse;
    }

    public GetMethodBuilder addGetHeader(String name, String value) {
        httpMethod.addHeader(name, value);
        return this;
    }

    @Override
    public PostMethodBuilder addHeader(String name, String value) {
        httpMethod.addHeader(name, value);
        return this;
    }

    @Override
    public PostMethodBuilder addParam(String name, String value) {
        if (httpMethod instanceof FormPost) {
            ((FormPost) httpMethod).addParameter(name, value);
        }
        return this;
    }

    @Override
    public PostMethodBuilder setParamValue(String name, String value) {
        if (httpMethod instanceof FormPost) {
            ((FormPost) httpMethod).setParameter(name, value);
        }
        return this;
    }

    @Override
    public PostMethodBuilder setCredentials(String user, String password) {
        httpClient.setBasicCredentials(user, password);
        return this;
    }

    @Override
    public PostMethodBuilder setUrl(String url) {
        if (httpMethod instanceof FormPost) {
            (httpMethod).setUrl(mergeURI(url).toString());
        }
        return this;
    }

    @Override
    public GetMethodBuilder addGetParam(String name, String value) {
        if (httpMethod instanceof Get) {
            ((Get) httpMethod).addParameter(name, value);
        }
        return this;
    }

    public List<Cookie> getCookies() {
        return this.response.getCookies().getCookies();
    }

    public void setTimeout(TimeLength timeOut) {
        httpClient.setTimeOut(timeOut);
    }

    public String getUrl() {
        return httpMethod.getUrl();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public void setFailureFlag(String failureFlag) {
        this.failureFlag = failureFlag;
    }

    public HttpRequest getHttpMethod() {
        return httpMethod;
    }


}
