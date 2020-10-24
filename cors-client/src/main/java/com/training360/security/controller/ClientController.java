package com.training360.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ClientController {

    @GetMapping("/simple-cors")
    public String simpleCors() {
        return "simple-cors";
    }

    @GetMapping("/preflight-cors")
    public String preflightCors() {
        return "preflight-cors";
    }
}
