package org.fortune.commons.core.setting.exception;

public class SettingsException extends RuntimeException {

    public SettingsException() {
        super();
    }

    public SettingsException(String message) {
        super(message);
    }

    public SettingsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SettingsException(Throwable cause) {
        super(cause);
    }

    protected SettingsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
