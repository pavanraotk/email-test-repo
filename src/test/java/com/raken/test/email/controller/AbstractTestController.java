package com.raken.test.email.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raken.test.email.controller.response.EmailResponse;
import com.raken.test.email.controller.response.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

abstract class AbstractTestController {

    protected final static ObjectMapper objectMapper = new ObjectMapper();

    MvcResult performPostRequest(MockMvc mvc, String path, Object request) throws Exception {
        return mvc.perform(post(path)
                .accept(MediaType.APPLICATION_JSON)
                .content(getJsonString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    void assertSuccessResponse(MvcResult mvcResult) throws IOException {
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        EmailResponse emailResponse = (EmailResponse) getObjectFromString(mvcResult.getResponse().getContentAsString(), EmailResponse.class);
        assertThat(emailResponse.getMessage()).isEqualTo("Successfully sent email");
    }

    void assertInternalServerErrorResponse(MvcResult mvcResult, String code, String message) throws Exception {
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(500);
        ErrorResponse errorResponse = (ErrorResponse) getObjectFromString(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);
        assertThat(errorResponse.getCode()).isEqualTo(code);
        assertThat(errorResponse.getMessage()).isEqualTo(message);

    }

    void assertBadRequestErrorResponse(MvcResult mvcResult, String code, String message) throws IOException {
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        ErrorResponse errorResponse = (ErrorResponse) getObjectFromString(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);
        assertThat(code).isEqualTo(errorResponse.getCode());
        assertThat(message).isEqualTo(errorResponse.getMessage());
    }

    private Object getObjectFromString(String responseString, Class className) throws IOException {
        return objectMapper.readValue(responseString, className);
    }

    private String getJsonString(Object request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }
}
