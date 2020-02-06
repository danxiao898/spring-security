package com.mengxuegu.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerLoginController {

    @GetMapping("/login/page")
    public String toLogin() {

        return "login";//templates/login.html
    }
}
