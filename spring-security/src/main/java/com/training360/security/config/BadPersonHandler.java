package com.training360.security.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BadPersonHandler implements AuthenticationFailureHandler {

    private final AuthenticationFailureHandler delegate = new SimpleUrlAuthenticationFailureHandler(
            "/login?error");

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        if(exception instanceof YouAreABadPersonException) {
            response.getWriter().write("You are a bad person");
        } else {
            delegate.onAuthenticationFailure(request, response, exception);
        }
    }
}
