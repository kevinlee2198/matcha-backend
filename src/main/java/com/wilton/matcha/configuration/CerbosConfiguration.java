package com.wilton.matcha.configuration;

import dev.cerbos.sdk.CerbosBlockingClient;
import dev.cerbos.sdk.CerbosClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CerbosConfiguration {
    private final String cerbosTargetUrl;

    @Autowired
    public CerbosConfiguration(MatchaConfigurationProperties matchaConfigurationProperties) {
        this.cerbosTargetUrl = matchaConfigurationProperties.getCerbosServerUrl();
    }

    @Bean
    public CerbosBlockingClient cerbosBlockingClient() throws CerbosClientBuilder.InvalidClientConfigurationException {
        return new CerbosClientBuilder(cerbosTargetUrl).withPlaintext().buildBlockingClient();
    }
}
