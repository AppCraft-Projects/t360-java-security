package com.training360.security._06_validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class LogForging {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogForging.class);

    public static void main(String[] args) {

        parseValue("one");
        parseValue("one\nUser logged out");
        secureParseValue("one\nUser logged out");
    }

    private static void parseValue(String val) {
        try {
            int value = Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            LOGGER.info("Failed to parse val=" + val);
        }
    }

    private static void secureParseValue(String val) {
        try {
            int value = Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            if (Pattern.matches("[A-Za-z0-9_]+", val)) {
                LOGGER.info("Failed to parse val=" + val);
            } else {
                LOGGER.info("User input failed!");
            }
        }
    }


}
