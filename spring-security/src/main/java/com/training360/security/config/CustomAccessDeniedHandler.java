package com.training360.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException e
    ) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            LOGGER.warn("User: {} attempted to access the protected URL: {}",
                    auth.getName(), request.getRequestURI());
        }

        response.sendRedirect(request.getContextPath() + "/accessDenied");
    }
}
