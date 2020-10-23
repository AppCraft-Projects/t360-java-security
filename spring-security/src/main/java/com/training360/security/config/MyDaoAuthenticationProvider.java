package com.training360.security.config;

import com.training360.security.service.LoginAttemptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyDaoAuthenticationProvider.class);

    private LoginAttemptService loginAttemptService;

    public MyDaoAuthenticationProvider(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            LOGGER.info("Checking user's ip for rate limiting...");
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            if(loginAttemptService.isBlocked(request.getRemoteAddr())) {
                LOGGER.warn("Request is blocked because of too much tries: {}", request.getRemoteAddr());
                throw new YouAreABadPersonException("And you are banned");
            }
        }
        LOGGER.info("Trying to authenticate with: {}", authentication);
        return super.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        LOGGER.info("Checking if supports with: {}", authentication);
        return super.supports(authentication);
    }
}
