package com.raken.test.email.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Email {


    private String fromAddress;
    private String toAddresses;
    private String ccAddresses;
    private String bccAddresses;
    private String subject;
    private String body;
    private Boolean sendGridProvider;
    private Boolean mailGunProvider;

    public Email(String fromAddress, String toAddresses, String ccAddresses, String bccAddresses, String subject, String body, Boolean sendGridProvider, Boolean mailGunProvider) {
        this.fromAddress = fromAddress;
        this.toAddresses = toAddresses;
        this.ccAddresses = ccAddresses;
        this.bccAddresses = bccAddresses;
        this.subject = subject;
        this.body = body;
        this.sendGridProvider = sendGridProvider;
        this.mailGunProvider = mailGunProvider;
    }
}
