package org.fortune.commons.crawler.exception;

/**
 * @author: landy
 * @date: 2019/11/21 22:36
 * @description:
 */
public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalServiceException(Throwable cause) {
        super(cause);
    }
}
