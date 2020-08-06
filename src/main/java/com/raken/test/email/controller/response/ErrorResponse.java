package com.raken.test.email.controller.response;

import com.fasterxml.jackson.annotation.JsonCreator;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.DEFAULT;

public class ErrorResponse extends AbstractResponse {
    String code;

    @JsonCreator(mode = DEFAULT)
    public ErrorResponse() {

    }

    public ErrorResponse(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public ErrorResponse setCode(String code) {
        this.code = code;
        return this;
    }
}
