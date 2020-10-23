package com.training360.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginAttemptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAttemptService.class);
    private static final AtomicInteger DEFAULT_COUNTER = new AtomicInteger();

    private final int MAX_ATTEMPTS = 5;
    private final ConcurrentMap<String, AtomicInteger> attemptsLookup = new ConcurrentHashMap<>();

    public void loginSucceeded(String key) {
        attemptsLookup.remove(key);
    }

    public void loginFailed(String key) {
        attemptsLookup.putIfAbsent(key, new AtomicInteger());
        AtomicInteger attempts = attemptsLookup.get(key);
        attempts.incrementAndGet();
        LOGGER.info("Login failed for key: {}. Attempts: {}}", key, attempts.get());
    }

    public boolean isBlocked(String key) {
        return attemptsLookup.getOrDefault(key, DEFAULT_COUNTER).get() >= MAX_ATTEMPTS;
    }
}
