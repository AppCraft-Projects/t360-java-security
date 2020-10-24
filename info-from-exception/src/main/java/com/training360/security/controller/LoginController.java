package com.training360.security.controller;

import com.training360.security.data.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private static final String EXPECTED_PASSWORD = "password";

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute("user") User user
    ) {
        if (user.getPassword().equals(EXPECTED_PASSWORD)) {
            return "login_success";
        } else {
            throw new IllegalArgumentException(
                    String.format("Password %s doesn't match expected password %s", user.getPassword(), EXPECTED_PASSWORD)
            );
        }
    }


}
