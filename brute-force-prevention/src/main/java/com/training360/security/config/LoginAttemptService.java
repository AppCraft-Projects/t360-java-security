package com.training360.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPTS = 5;
    private final ConcurrentMap<String, Integer> attemptsLookup = new ConcurrentHashMap<>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void loginSucceeded(String key) {
        attemptsLookup.remove(key);
    }

    public void loginFailed(String key) {
        Integer attempts = attemptsLookup.get(key);
        if (attempts == null) {
            attempts = 0;
        }
        attempts++;
        logger.info(String.format("Login failed for key: %s. Attempts: %d", key, attempts));
        attemptsLookup.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        return attemptsLookup.get(key) >= MAX_ATTEMPTS;
    }
}
