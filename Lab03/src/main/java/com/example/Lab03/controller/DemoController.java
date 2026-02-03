package com.example.lab03.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    @GetMapping("/demo")
    public String demo(Model model) {
        model.addAttribute("message", "Welcome Thymeleaf!");
        model.addAttribute("student", "Nguyễn Văn A");
        return "demo";
    }
}