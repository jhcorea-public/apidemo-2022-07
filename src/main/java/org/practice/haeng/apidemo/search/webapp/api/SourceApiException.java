package org.practice.haeng.apidemo.search.webapp.api;

public class SourceApiException extends RuntimeException{

    public SourceApiException(Throwable cause) {
        super(cause);
    }

    public SourceApiException(String message) {
        super(message);
    }
}
