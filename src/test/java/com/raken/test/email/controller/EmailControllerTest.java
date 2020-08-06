package com.raken.test.email.controller;

import com.raken.test.email.builder.EmailRequestBuilder;
import com.raken.test.email.controller.request.EmailRequest;
import com.raken.test.email.model.Email;
import com.raken.test.email.service.EmailService;
import com.raken.test.email.service.exception.EmailServiceException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static java.util.Locale.ENGLISH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class EmailControllerTest extends AbstractTestController {

    private MockMvc mvc;
    private EmailController emailController;
    private MessageSource messageSource;
    private EmailService emailService;
    private EmailRequest emailRequest;

    @Before
    public void setup() throws EmailServiceException {
        emailService = mock(EmailService.class);
        messageSource = mock(MessageSource.class);
        emailController = new EmailController(messageSource, emailService);
        emailRequest = new EmailRequestBuilder().withData().build();

        mvc = MockMvcBuilders.standaloneSetup(emailController)
                .alwaysDo(print())
                .build();

        doNothing().when(emailService).sendEmail(any(Email.class));
    }

    @Test
    public void testSuccess() throws Exception {
        when(messageSource.getMessage("send.email.successful", null, ENGLISH)).thenReturn("Successfully sent email");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequest);
        assertSuccessResponse(mvcResult);
    }

    @Test
    public void testFromAddressNull() throws Exception {
        when(messageSource.getMessage("fromAddress.should.exist", null, ENGLISH)).thenReturn("From Address cannot be blank");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.remove("fromAddress");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "fromAddress.should.exist", "From Address cannot be blank");
    }

    @Test
    public void testFromAddressBlank() throws Exception {
        when(messageSource.getMessage("fromAddress.should.exist", null, ENGLISH)).thenReturn("From Address cannot be blank");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.put("fromAddress", "");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "fromAddress.should.exist", "From Address cannot be blank");
    }

    @Test
    public void testFromAddressNotValidFormat() throws Exception {
        when(messageSource.getMessage("fromAddress.invalid.format", null, ENGLISH)).thenReturn("Format of from address is wrong");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.put("fromAddress", "test");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "fromAddress.invalid.format", "Format of from address is wrong");
    }

    @Test
    public void testToAddressNull() throws Exception {
        when(messageSource.getMessage("toAddresses.should.exist", null, ENGLISH)).thenReturn("To Addresses cannot be blank");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.remove("toAddresses");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "toAddresses.should.exist", "To Addresses cannot be blank");
    }

    @Test
    public void testToAddressBlank() throws Exception {
        when(messageSource.getMessage("toAddresses.should.exist", null, ENGLISH)).thenReturn("To Addresses cannot be blank");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.put("toAddresses", "");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "toAddresses.should.exist", "To Addresses cannot be blank");
    }

    @Test
    public void testToAddressInvalid() throws Exception {
        when(messageSource.getMessage("toAddresses.invalid.format", null, ENGLISH)).thenReturn("Format of to addresses is wrong");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.put("toAddresses", "test");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "toAddresses.invalid.format", "Format of to addresses is wrong");
    }

    @Test
    public void testToAddressInvalidWithMultipleAddresses() throws Exception {
        when(messageSource.getMessage("toAddresses.invalid.format", null, ENGLISH)).thenReturn("Format of to addresses is wrong");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.put("toAddresses", "test@test.com,test");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "toAddresses.invalid.format", "Format of to addresses is wrong");
    }

    @Test
    public void testCcAddressInvalid() throws Exception {
        when(messageSource.getMessage("ccAddresses.invalid.format", null, ENGLISH)).thenReturn("Format of cc addresses is wrong");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.put("ccAddresses", "test");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "ccAddresses.invalid.format", "Format of cc addresses is wrong");
    }

    @Test
    public void testccAddressInvalidWithMultipleAddresses() throws Exception {
        when(messageSource.getMessage("ccAddresses.invalid.format", null, ENGLISH)).thenReturn("Format of cc addresses is wrong");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.put("ccAddresses", "test@test.com,test");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "ccAddresses.invalid.format", "Format of cc addresses is wrong");
    }

    @Test
    public void testBccAddressInvalid() throws Exception {
        when(messageSource.getMessage("bccAddresses.invalid.format", null, ENGLISH)).thenReturn("Format of bcc addresses is wrong");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.put("bccAddresses", "test");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "bccAddresses.invalid.format", "Format of bcc addresses is wrong");
    }

    @Test
    public void testBccAddressInvalidWithMultipleAddresses() throws Exception {
        when(messageSource.getMessage("bccAddresses.invalid.format", null, ENGLISH)).thenReturn("Format of bcc addresses is wrong");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.put("bccAddresses", "test@test.com,test");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "bccAddresses.invalid.format", "Format of bcc addresses is wrong");
    }

    @Test
    public void testSubjectNull() throws Exception {
        when(messageSource.getMessage("subject.should.exist", null, ENGLISH)).thenReturn("Subject cannot be blank");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.remove("subject");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "subject.should.exist", "Subject cannot be blank");
    }

    @Test
    public void testSubjectBlank() throws Exception {
        when(messageSource.getMessage("subject.should.exist", null, ENGLISH)).thenReturn("Subject cannot be blank");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.put("subject", "");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "subject.should.exist", "Subject cannot be blank");
    }

    @Test
    public void testSubjectLengthGreaterThan50() throws Exception {
        when(messageSource.getMessage("subject.length.does.not.match", null, ENGLISH)).thenReturn("Subject length should be between 1 to 50 characters");
        Map emailRequestMap = objectMapper.convertValue(emailRequest, Map.class);
        emailRequestMap.put("subject", "This is greater than 50 characters, should not be greater than that");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequestMap);
        assertBadRequestErrorResponse(mvcResult, "subject.length.does.not.match", "Subject length should be between 1 to 50 characters");
    }

    @Test
    public void testEmailServiceThrowsException() throws Exception {
        doThrow(new EmailServiceException("error.sending.email", "Unable to send email, please retry again later")).when(emailService).sendEmail(any(Email.class));
        when(messageSource.getMessage("error.sending.email", null, ENGLISH)).thenReturn("Unable to send email, please retry again later");
        MvcResult mvcResult = performPostRequest(mvc, "/email/sendEmail", emailRequest);
        assertInternalServerErrorResponse(mvcResult, "error.sending.email", "Unable to send email, please retry again later");
    }


}