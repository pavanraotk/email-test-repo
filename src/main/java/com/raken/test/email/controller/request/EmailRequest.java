package com.raken.test.email.controller.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmailRequest {

    @Email
    @NotBlank
    private String fromAddress;

    @NotBlank
    private String toAddresses;

    private String ccAddresses;
    private String bccAddresses;

    @NotBlank
    @Size(max = 50)
    private String subject;

    @Size(max = 1000)
    private String body;

}
