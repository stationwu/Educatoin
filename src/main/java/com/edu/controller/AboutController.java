package com.edu.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AboutController {
	
	@GetMapping("/site/about")
	public String about(Model model) {
		return "site_about";
	}
}
