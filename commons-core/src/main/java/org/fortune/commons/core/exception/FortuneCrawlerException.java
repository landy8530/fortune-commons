package org.fortune.commons.core.exception;

/**
 * @author: landy
 * @date: 2019/11/23 09:22
 * @description:
 */
public class FortuneCrawlerException extends RuntimeException {
    public FortuneCrawlerException(String message) {
        super(message);
    }

    public FortuneCrawlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public FortuneCrawlerException(Throwable cause) {
        super(cause);
    }
}
