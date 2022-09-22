package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@GetMapping("/showLoginPage")
	public String showLoginPage() {
		return "security/login-form";
	}
	@GetMapping("/access-denied")
	public String accessDenied() {
		return "security/access-denied";
	}
}
