package com.training360.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClickjackController {

    @GetMapping("/clickjack")
    public String clickjack() {
        return "clickjack";
    }

    @GetMapping("/attack")
    public String attack() {
        return "attack";
    }
}
