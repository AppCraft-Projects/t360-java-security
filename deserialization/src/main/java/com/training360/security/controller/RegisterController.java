package com.training360.security.controller;

import com.training360.security.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class RegisterController {

    @GetMapping("/register")
    public String showRegister() {
        return "registerForm";
    }

    @GetMapping("/register-success")
    public String registerSuccess() {
        return "registerSuccess";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("message",
                String.format("Welcome, %s (admin:%b)!", user.getName(), user.getAdmin()));
        return "redirect:/register-success";
    }


}
