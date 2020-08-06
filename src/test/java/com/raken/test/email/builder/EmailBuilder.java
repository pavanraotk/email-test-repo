package com.raken.test.email.builder;


import com.raken.test.email.model.Email;

public class EmailBuilder {
    private String fromAddress;
    private String toAddresses;
    private String ccAddresses;
    private String bccAddresses;
    private String subject;
    private String body;

    public EmailBuilder setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
        return this;
    }

    public EmailBuilder setToAddresses(String toAddresses) {
        this.toAddresses = toAddresses;
        return this;
    }

    public EmailBuilder setCcAddresses(String ccAddresses) {
        this.ccAddresses = ccAddresses;
        return this;
    }

    public EmailBuilder setBccAddresses(String bccAddresses) {
        this.bccAddresses = bccAddresses;
        return this;
    }

    public EmailBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailBuilder setBody(String body) {
        this.body = body;
        return this;
    }


    public EmailBuilder withData() {
        this.fromAddress = "test@test.com";
        this.toAddresses = "test2@test.com;test3@test.com";
        this.ccAddresses = "test5@test.com;test4@test.com";
        this.bccAddresses = "test6@test.com;test7@test.com";
        this.subject = "Test subject";
        this.body = "Test body";
        return this;
    }

    public Email build() {
        return new Email(fromAddress, toAddresses, ccAddresses, bccAddresses, subject, body, null, null);
    }
}
