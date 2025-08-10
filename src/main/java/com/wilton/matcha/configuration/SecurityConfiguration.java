package com.wilton.matcha.configuration;

import com.wilton.matcha.common.security.MatchaUserJwtAuthenticationConverter;
import com.wilton.matcha.configuration.api.exception.MatchaDelegatingHandlerExceptionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    private final CorsFilter corsFilter;
    private final MatchaDelegatingHandlerExceptionResolver matchaDelegatingHandlerExceptionResolver;

    @Autowired
    public SecurityConfiguration(
            CorsFilter corsFilter, MatchaDelegatingHandlerExceptionResolver matchaDelegatingHandlerExceptionResolver) {
        this.corsFilter = corsFilter;
        this.matchaDelegatingHandlerExceptionResolver = matchaDelegatingHandlerExceptionResolver;
    }

    // https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(corsFilter, CsrfFilter.class)
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oAuth2ResourceServerConfigurer -> oAuth2ResourceServerConfigurer
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new MatchaUserJwtAuthenticationConverter()))
                        .authenticationEntryPoint(matchaDelegatingHandlerExceptionResolver)
                        .accessDeniedHandler(matchaDelegatingHandlerExceptionResolver))
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                                .requestMatchers(HttpMethod.OPTIONS, "/**")
                                .permitAll()
                                .requestMatchers("/i18n/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/favicon.ico")
                                .permitAll()
                                // actuator
                                .requestMatchers(HttpMethod.GET, "/actuator/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .authenticationEntryPoint(matchaDelegatingHandlerExceptionResolver)
                        .accessDeniedHandler(matchaDelegatingHandlerExceptionResolver));
        return http.build();
    }
}
