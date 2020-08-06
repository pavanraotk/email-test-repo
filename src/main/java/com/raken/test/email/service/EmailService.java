package com.raken.test.email.service;

import com.raken.test.email.config.Configuration;
import com.raken.test.email.model.Email;
import com.raken.test.email.service.exception.EmailServiceException;
import com.raken.test.email.service.provider.SendGridEmailProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final Configuration configuration;
    private final SendGridEmailProvider sendGridEmailProvider;

    @Autowired
    public EmailService(Configuration configuration, SendGridEmailProvider sendGridEmailProvider) {
        this.configuration = configuration;
        this.sendGridEmailProvider = sendGridEmailProvider;
    }

    public void sendEmail(Email email) throws EmailServiceException {
        try {
            logger.debug("Sending email for {}", email.toString());
            logEmail(email);
            sendGridEmailProvider.sendEmail(email);
        } catch (Exception e) {
            logger.error("Error in sending email {}", e);
            throw new EmailServiceException("error.sending.email", "Internal server error in sending email", e);
        }
    }

    private void logEmail(Email email) {
        if (configuration.getBooleanProperty("log.non.raken.recipients", true)) {
            String toAddresses = getEmailAddresses(email.getToAddresses());
            String ccAddresses = getEmailAddresses(email.getCcAddresses());
            String bccAddresses = getEmailAddresses(email.getBccAddresses());
            List<String> nonRakenEmailRecipients = checkIfNonRakenRecipients(toAddresses + ";" + ccAddresses + ";" + bccAddresses);
            if (!nonRakenEmailRecipients.isEmpty()) {
                nonRakenEmailRecipients.forEach(nonRakenEmailRecipient -> {
                    logger.warn("Email from {} is being sent to this non raken email recipient {} with body {}", email.getFromAddress(), nonRakenEmailRecipient, email.getBody());
                });
            }
        }
    }

    private String getEmailAddresses(String emailAddress) {
        return StringUtils.isEmpty(emailAddress) ? "" : emailAddress;
    }

    private List<String> checkIfNonRakenRecipients(String emailAddresses) {
        List<String> emailAddressesList = (List<String>) Arrays.asList(emailAddresses.split(";"));
        return emailAddressesList.stream()
                .filter(emailAddress -> !emailAddress.contains("@rakenapp.com"))
                .collect(Collectors.toList());

    }


}
