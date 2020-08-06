package com.raken.test.email.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Configuration {

    @Autowired
    private Environment environment;

    public String getStringProperty(String key, String defaultValue) {
        return environment.getProperty(key) == null ? defaultValue : environment.getProperty(key);
    }

    public Integer getIntProperty(String key, Integer defaultValue) {
        return environment.getProperty(key) == null ? defaultValue : Integer.parseInt(environment.getProperty(key));
    }

    public Boolean getBooleanProperty(String key, Boolean defaultValue) {
        return environment.getProperty(key) == null ? defaultValue : Boolean.valueOf(environment.getProperty(key));
    }
}
