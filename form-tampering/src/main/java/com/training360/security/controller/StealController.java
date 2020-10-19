package com.training360.security.controller;

import com.training360.security.data.UserWithCardNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StealController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/steal-your-data")
    public void steal(
            @ModelAttribute("user") UserWithCardNumber user
    ) {
        logger.info(String.format("Your card number is %s", user.getCard()));
    }
}
