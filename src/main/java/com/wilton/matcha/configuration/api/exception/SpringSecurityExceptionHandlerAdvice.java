package com.wilton.matcha.configuration.api.exception;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Integrates Spring Security with Problem Details support into Spring Web
 * See <a href="https://www.baeldung.com/spring-security-exceptionhandler">Link</a>
 */
@RestControllerAdvice
public class SpringSecurityExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    public ProblemDetail handleInvalidBearerTokenException(InvalidBearerTokenException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        problemDetail.setType(URI.create(BearerTokenErrorCodes.INVALID_TOKEN));
        return problemDetail;
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ProblemDetail handleOAuth2AuthenticationException(OAuth2AuthenticationException e) {
        OAuth2Error error = e.getError();
        ProblemDetail problemDetail;
        if (error instanceof BearerTokenError) {
            problemDetail =
                    ProblemDetail.forStatusAndDetail(((BearerTokenError) error).getHttpStatus(), e.getMessage());
        } else {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        problemDetail.setType(URI.create(e.getError().getErrorCode()));
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        problemDetail.setType(URI.create("access_denied"));
        return problemDetail;
    }
}
