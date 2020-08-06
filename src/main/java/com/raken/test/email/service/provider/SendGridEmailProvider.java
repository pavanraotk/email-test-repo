package com.raken.test.email.service.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raken.test.email.config.Configuration;
import com.raken.test.email.model.Email;
import com.raken.test.email.service.exception.EmailServiceException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SendGridEmailProvider {

    private static final Logger logger = LoggerFactory.getLogger(SendGridEmailProvider.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private Configuration configuration;

    public void sendEmail(Email email) throws EmailServiceException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            logger.debug("Email to be sent from send grid is {}", email.toString());
            HttpPost httpPost = new HttpPost(configuration.getStringProperty("sendgrid.api.url", "https://api.sendgrid.com:443/v3/mail/send"));
            httpPost.setHeader("Authorization", "Bearer " + configuration.getStringProperty("sendgrid.api.key", ""));
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(generateEntity(email));
            CloseableHttpResponse response = client.execute(httpPost);
            response.getStatusLine().getStatusCode();
            configuration.getIntProperty("sendgrid.api.response.code", 202);
        } catch (UnsupportedEncodingException e) {
            logger.error("Unsupported encoding in send grid email service {}", e);
            throw new EmailServiceException("unsupported.encoding", "Unsupported encoding error send grid");
        } catch (ClientProtocolException e) {
            logger.error("Client Protocol exception in send grid email service {}", e);
            throw new EmailServiceException("unsupported.client.protocol", "Client protocol error in send grid");
        } catch (IOException e) {
            logger.error("IO Exception exception in mail send grid email service {}", e);
            throw new EmailServiceException("io.error", "IO Exception in send grid");
        }
    }

    private StringEntity generateEntity(Email email) throws UnsupportedEncodingException, JsonProcessingException {
        return new StringEntity(generateJsonAsStringMap(email));
    }

    private String generateJsonAsStringMap(Email email) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("from", getFromMap(email));
        map.put("subject", email.getSubject());
        map.put("personalizations", getPersonalisations(email));
        map.put("content", getContent(email.getBody()));
        return objectMapper.writeValueAsString(map);
    }

    private Object getContent(String body) {
        List<Map<String, String>> contents = new ArrayList<>();
        contents.add(generateContent(body));
        return contents;
    }

    private Map<String, String> generateContent(String body) {
        Map<String, String> contents = new HashMap<>();
        contents.put("type", "text/plain");
        contents.put("value", body);
        return contents;
    }

    private Object getPersonalisations(Email email) {
        List<Map<String, List<Map<String, Object>>>> personalizations = new ArrayList<>();
        personalizations.add(generateAddressesMap(email));
        return personalizations;
    }

    private Map<String, List<Map<String, Object>>> generateAddressesMap(Email email) {
        List<Map<String, Object>> toAddressesList = getAddressMaps(email.getToAddresses());
        List<Map<String, Object>> bccAddressesList = getAddressMaps(email.getBccAddresses());
        List<Map<String, Object>> ccAddressesList = getAddressMaps(email.getCcAddresses());
        Map<String, List<Map<String, Object>>> toAddressesMap = new HashMap<>();
        toAddressesMap.put("to", toAddressesList);
        toAddressesMap.put("bcc", bccAddressesList);
        toAddressesMap.put("cc", ccAddressesList);
        return toAddressesMap;
    }

    private List<Map<String, Object>> getAddressMaps(String toAddresses) {
        if(toAddresses!=null) {
            String[] toAddressesSplit = toAddresses.split(",");
            List<Map<String, Object>> toAddressesList = new ArrayList<>();
            for (String toAddress : toAddressesSplit) {
                toAddressesList.add(generateToAddress(toAddress));
            }
            return toAddressesList;
        }
        return null;
    }

    private Map<String, Object> generateToAddress(String toAddress) {
        Map<String, Object> address = new HashMap<>();
        address.put("email", toAddress);
        return address;
    }

    private Object getFromMap(Email email) {
        Map<String, String> fromMap = new HashMap<>();
        fromMap.put("email", email.getFromAddress());
        return fromMap;
    }

}
