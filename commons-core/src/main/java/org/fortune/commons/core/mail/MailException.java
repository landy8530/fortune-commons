package org.fortune.commons.core.mail;

/**
 * @author landyl
 * @create 3:56 PM 03/24/2020
 */
public class MailException extends RuntimeException {
    public MailException(Throwable cause) {
        super(cause);
    }

    public MailException(String message) {
        super(message);
    }
}
