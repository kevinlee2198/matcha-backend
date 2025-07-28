package com.wilton.matcha.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private final MatchaConfigurationProperties configurationProperties;

    @Autowired
    public WebConfiguration(MatchaConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @Override
    // see https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-config-conversion
    public void addFormatters(FormatterRegistry registry) {
        WebMvcConfigurer.super.addFormatters(registry);

        // converters are used to convert between contract obj/dtos of different modules
        // alphabetical order
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = configurationProperties.getCors();
        if (!CollectionUtils.isEmpty(config.getAllowedOrigins()) || !CollectionUtils.isEmpty(config.getAllowedOriginPatterns())) {
            source.registerCorsConfiguration("/api/**", config);
            source.registerCorsConfiguration("/graphql/**", config);
        }
        return new CorsFilter(source);
    }
}
