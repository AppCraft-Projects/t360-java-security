package com.training360.security.config;

import org.springframework.security.core.AuthenticationException;

public class YouAreABadPersonException extends AuthenticationException {
    public YouAreABadPersonException(String msg) {
        super(msg);
    }
}
