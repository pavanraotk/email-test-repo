package com.raken.test.email.builder;

import com.raken.test.email.controller.request.EmailRequest;

public class EmailRequestBuilder {
    private String fromAddress;
    private String toAddresses;
    private String ccAddresses;
    private String bccAddresses;
    private String subject;
    private String body;

    public EmailRequestBuilder setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
        return this;
    }

    public EmailRequestBuilder setToAddresses(String toAddresses) {
        this.toAddresses = toAddresses;
        return this;
    }

    public EmailRequestBuilder setCcAddresses(String ccAddresses) {
        this.ccAddresses = ccAddresses;
        return this;
    }

    public EmailRequestBuilder setBccAddresses(String bccAddresses) {
        this.bccAddresses = bccAddresses;
        return this;
    }

    public EmailRequestBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailRequestBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public EmailRequestBuilder withData() {
        this.fromAddress = "pavanraotk@gmail.com";
        this.toAddresses = "pavan.rao333@gmail.com";
        this.subject = "Controller IT";
        this.body = "Controller integration test";
        return this;
    }

    public EmailRequest build() {
        return new EmailRequest(fromAddress, toAddresses, ccAddresses, bccAddresses, subject, body);
    }
}
