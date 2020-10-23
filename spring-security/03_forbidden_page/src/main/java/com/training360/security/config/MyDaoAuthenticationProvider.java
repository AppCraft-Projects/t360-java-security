package com.training360.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private static Logger LOGGER = LoggerFactory.getLogger(MyDaoAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOGGER.info("Trying to authenticate with: {}", authentication);
        return super.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        LOGGER.info("Checking if supports with: {}", authentication);
        return super.supports(authentication);
    }
}
