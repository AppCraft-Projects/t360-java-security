package com.training360.security.controller;

import com.training360.security.data.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class XSSController {

    @GetMapping("/register")
    public String showRegister() {
        return "registerForm";
    }

    @GetMapping("/register-success")
    public String registerSuccess() {
        return "registerSuccess";
    }

    @GetMapping("/haxxed")
    public String haxxed() {
        return "haxxed";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("message",
                String.format("Welcome, %s!", user.getName()));
        return "redirect:/register-success";
    }



}
