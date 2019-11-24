package org.fortune.commons.core.exception;

/**
 * @author: landy
 * @date: 2019/11/24 15:28
 * @description:
 */
public class XMLException extends RuntimeException {
    public XMLException(String message) {
        super(message);
    }

    public XMLException(Throwable cause) {
        super(cause);
    }
}
