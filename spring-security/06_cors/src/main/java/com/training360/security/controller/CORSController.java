package com.training360.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CORSController {

    @GetMapping("/api/simple-cors")
    public String simpleCors() {
        return "ok";
    }
}
