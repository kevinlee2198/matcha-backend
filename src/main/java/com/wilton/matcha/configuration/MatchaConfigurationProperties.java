package com.wilton.matcha.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

@Component
@ConfigurationProperties(prefix = "matcha", ignoreUnknownFields = false)
public class MatchaConfigurationProperties {
    @NestedConfigurationProperty
    private final CorsConfiguration cors = new CorsConfiguration();

    private String fileStorageLocalPath;

    private String cerbosServerUrl;

    public CorsConfiguration getCors() {
        return cors;
    }

    public String getFileStorageLocalPath() {
        return fileStorageLocalPath;
    }

    public void setFileStorageLocalPath(String fileStorageLocalPath) {
        this.fileStorageLocalPath = fileStorageLocalPath;
    }

    public String getCerbosServerUrl() {
        return cerbosServerUrl;
    }

    public void setCerbosServerUrl(String cerbosServerUrl) {
        this.cerbosServerUrl = cerbosServerUrl;
    }
}
