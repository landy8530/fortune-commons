package org.landy.commons.core.setting.exception;

public class ResolveFailedConfigException extends RuntimeException {

    public ResolveFailedConfigException() {
        super();
    }

    public ResolveFailedConfigException(String message) {
        super(message);
    }

    public ResolveFailedConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResolveFailedConfigException(Throwable cause) {
        super(cause);
    }

    protected ResolveFailedConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
