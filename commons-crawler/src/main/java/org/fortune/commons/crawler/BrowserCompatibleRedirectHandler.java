package org.fortune.commons.crawler;


import org.fortune.commons.core.http.HttpHeaders;
import org.fortune.commons.core.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BrowserCompatibleRedirectHandler will do redirection for all redirect status code,
 * no matter the current method (in HTTP spec, non GET/HEAD method requires user intervention)
 */
public class BrowserCompatibleRedirectHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserCompatibleRedirectHandler.class);

    public HttpResponse handleRedirect(CrawlerClient client, HttpResponse response) {
        if (response.getStatusCode().isRedirect()) {
            String url = getRedirectURL(response.getHeaders());
            LOGGER.info("Status Code is {}, isRedirect: true, should be redirected to the new URL: {}", response.getStatusCode().getStatusCode(), url);
            return client.url(url).submit();
        }
        return response;
    }

    String getRedirectURL(HttpHeaders headers) {
        String url = headers.getFirstHeaderValue("Location");
        if (url == null)
            url = headers.getFirstHeaderValue("location");
        return url == null ? null : url.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D");
    }
}
