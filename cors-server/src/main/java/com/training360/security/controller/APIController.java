package com.training360.security.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIController {

    @CrossOrigin(origins = "http://localhost:9090")
    @GetMapping("/api/simple-cors")
    public String simpleCors() {
        return "simple cors";
    }

    @CrossOrigin(origins = "http://localhost:9090")
    @PostMapping("/api/preflight-cors")
    public String preflightCors() {
        return "preflight cors";
    }
}
