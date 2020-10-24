package com.training360.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PwnController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/pwn")
    public void pwn() {
        logger.info("You have been pwned.");
    }

}
