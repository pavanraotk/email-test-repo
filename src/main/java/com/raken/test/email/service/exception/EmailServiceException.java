package com.raken.test.email.service.exception;

import lombok.Data;

@Data
public class EmailServiceException extends RuntimeException {

    private String code;
    private String message;

    public EmailServiceException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public EmailServiceException(String code, String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = code;
    }
}
