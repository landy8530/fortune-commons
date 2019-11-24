package org.fortune.commons.crawler.exception;

/**
 * @author: landy
 * @date: 2019/11/21 22:08
 * @description:
 */
public class WebSiteErrorException extends RuntimeException {
    public WebSiteErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
