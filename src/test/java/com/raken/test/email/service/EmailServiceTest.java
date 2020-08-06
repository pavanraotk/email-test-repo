package com.raken.test.email.service;

import com.raken.test.email.builder.EmailBuilder;
import com.raken.test.email.config.Configuration;
import com.raken.test.email.model.Email;
import com.raken.test.email.service.exception.EmailServiceException;
import com.raken.test.email.service.provider.SendGridEmailProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class EmailServiceTest {

    private EmailService emailService;
    private SendGridEmailProvider sendGridEmailProvider;
    private Configuration configuration;

    private Email email;

    @Before
    public void setUp() {
        sendGridEmailProvider = mock(SendGridEmailProvider.class);
        configuration = mock(Configuration.class);
        emailService = new EmailService(configuration, sendGridEmailProvider);

        email = new EmailBuilder().withData().build();
    }

    @Test
    public void testEmailServiceSuccess() throws EmailServiceException {
        doNothing().when(sendGridEmailProvider).sendEmail(email);
        when(configuration.getBooleanProperty("log.non.raken.recipients", true)).thenReturn(false);
        emailService.sendEmail(email);
    }

    @Test
    public void testEmailServiceSuccessLogEmail() throws EmailServiceException {
        doNothing().when(sendGridEmailProvider).sendEmail(email);
        when(configuration.getBooleanProperty("log.non.raken.recipients", true)).thenReturn(true);
        emailService.sendEmail(email);
    }

    @Test
    public void testEmailServiceThrowsException() throws EmailServiceException {
        doThrow(new EmailServiceException("error", "error")).when(sendGridEmailProvider).sendEmail(email);
        try {
            emailService.sendEmail(email);
        } catch (EmailServiceException e) {
            assertEquals("error.sending.email", e.getCode());
            assertEquals("Internal server error in sending email", e.getMessage());
        }
    }

}