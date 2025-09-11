package com.webknot.campus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    /**
     * Handle root URL and redirect to landing page
     */
    @GetMapping("/")
    public String home() {
        return "index.html";
    }
    
    /**
     * Handle admin route - redirect to login
     */
    @GetMapping("/admin")
    public String admin() {
        return "admin-login.html";
    }
    
    /**
     * Handle admin login route
     */
    @GetMapping("/admin-login")
    public String adminLogin() {
        return "admin-login.html";
    }
    
    /**
     * Handle student route - redirect to login
     */
    @GetMapping("/student")
    public String student() {
        return "student-login.html";
    }
    
    /**
     * Handle student login route
     */
    @GetMapping("/student-login")
    public String studentLogin() {
        return "student-login.html";
    }
}
