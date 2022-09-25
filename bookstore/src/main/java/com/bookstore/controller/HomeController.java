package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String showStartingPageNoName() {
		return "redirect:/auth/showLoginPage";
	}
	
	@GetMapping("/home")
	public String showStartingPage() {
		return "home/start";
	}
}
