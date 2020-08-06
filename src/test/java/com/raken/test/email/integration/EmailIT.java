package com.raken.test.email.integration;

import com.raken.test.email.builder.EmailRequestBuilder;
import com.raken.test.email.controller.EmailController;
import com.raken.test.email.controller.request.EmailRequest;
import com.raken.test.email.controller.response.AbstractResponse;
import com.raken.test.email.controller.response.EmailResponse;
import com.raken.test.email.controller.response.ErrorResponse;
import com.raken.test.email.service.exception.EmailServiceException;
import com.raken.test.email.service.provider.SendGridEmailProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EmailIT {

    @Autowired
    private EmailController emailController;

    @MockBean
    private SendGridEmailProvider sendGridEmailProvider;

    @Test
    public void testEmailController() throws EmailServiceException {
        doNothing().when(sendGridEmailProvider).sendEmail(any());
        EmailRequest emailRequest = new EmailRequestBuilder().withData().build();
        ResponseEntity<AbstractResponse> responseEntity = emailController.sendEmail(emailRequest);
        EmailResponse emailResponse = (EmailResponse) responseEntity.getBody();
        assertEquals("Successfully sent email", emailResponse.getMessage());
    }

}
