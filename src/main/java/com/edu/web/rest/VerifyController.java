package com.edu.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerifyController {

	@GetMapping("/MP_verify_HHA3VDSY1Q3Zn9r1.txt")
	public String read(){
		return "HHA3VDSY1Q3Zn9r1";
	}

	@GetMapping("/MP_verify_T7ILea1OMIoUi2kI.txt")
	public String verify(){
		return "T7ILea1OMIoUi2kI";
	}
}
