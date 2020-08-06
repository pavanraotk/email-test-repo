package com.raken.test.email.controller;


import com.raken.test.email.controller.request.EmailRequest;
import com.raken.test.email.controller.response.AbstractResponse;
import com.raken.test.email.controller.response.EmailResponse;
import com.raken.test.email.controller.response.ErrorResponse;
import com.raken.test.email.model.Email;
import com.raken.test.email.service.EmailService;
import com.raken.test.email.service.exception.EmailServiceException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static java.util.Locale.ENGLISH;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Import(EmailService.class)
@RequestMapping("/email")
public class EmailController extends BaseController {

    private EmailService emailService;

    @Autowired
    public EmailController(MessageSource messageSource, EmailService emailService) {
        super(messageSource);
        this.emailService = emailService;
    }

    @RequestMapping(value = "/sendEmail", method = POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<AbstractResponse> sendEmail(@Valid @RequestBody EmailRequest emailRequest) throws EmailServiceException {
        String emailAddressInvalidCode = validateEmailAddressesInRequest(emailRequest);
        if (emailAddressInvalidCode != null) {
            return new ResponseEntity<>(new ErrorResponse(emailAddressInvalidCode, messageSource.getMessage(emailAddressInvalidCode, null, ENGLISH)), BAD_REQUEST);
        }
        emailService.sendEmail(generateEmail(emailRequest));
        return new ResponseEntity<>(new EmailResponse(messageSource.getMessage("send.email.successful", null, ENGLISH)), OK);
    }

    private String validateEmailAddressesInRequest(EmailRequest emailRequest) {
        if (!validateEmailAddresses(emailRequest.getToAddresses())) {
            return "toAddresses.invalid.format";
        }
        if (emailRequest.getCcAddresses() != null) {
            if (!validateEmailAddresses(emailRequest.getCcAddresses())) {
                return "ccAddresses.invalid.format";
            }
        }
        if (emailRequest.getBccAddresses() != null) {
            if (!validateEmailAddresses(emailRequest.getBccAddresses())) {
                return "bccAddresses.invalid.format";
            }
        }
        return null;
    }

    private Email generateEmail(EmailRequest emailRequest) {
        Email email = new Email();
        email.setBccAddresses(emailRequest.getBccAddresses());
        email.setCcAddresses(emailRequest.getCcAddresses());
        email.setBody(emailRequest.getBody());
        email.setSubject(emailRequest.getSubject());
        email.setFromAddress(emailRequest.getFromAddress());
        email.setToAddresses(emailRequest.getToAddresses());
        return email;
    }

    private boolean validateEmailAddresses(String emailAddresses) {
        String[] emailAddressesSplit = emailAddresses.split(";");
        for (String toAddress : emailAddressesSplit) {
            boolean isValidEmail = EmailValidator.getInstance().isValid(toAddress);
            if (!isValidEmail) {
                return false;
            }
        }
        return true;

    }
}
