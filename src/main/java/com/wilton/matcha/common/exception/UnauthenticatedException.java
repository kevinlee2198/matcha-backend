package com.wilton.matcha.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthenticatedException extends AuthenticationException {
    public UnauthenticatedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnauthenticatedException(String msg) {
        super(msg);
    }

    public UnauthenticatedException() {
        super("User is not authenticated");
    }
}
