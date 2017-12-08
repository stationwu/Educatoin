package com.edu.errorhandler;

public class RedirectToErrorPageException extends RuntimeException {
    public RedirectToErrorPageException() {
    }

    public RedirectToErrorPageException(String message) {
        super(message);
    }

    public RedirectToErrorPageException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedirectToErrorPageException(Throwable cause) {
        super(cause);
    }

    public RedirectToErrorPageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
