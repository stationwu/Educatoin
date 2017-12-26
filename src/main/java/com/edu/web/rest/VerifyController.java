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

	@GetMapping("/.well-known/acme-challenge/mkNBT-2Ooxe1V7-KGHmEvdbsPqaLWhAqA7IwSGyMeh0")
	public String acmeVerify1() {
		return "mkNBT-2Ooxe1V7-KGHmEvdbsPqaLWhAqA7IwSGyMeh0.ozVtOSdqKRbxQDbQ2dmLVtob0bmuR46bYDMNtwmwlho";
	}

	@GetMapping("/.well-known/acme-challenge/phHGlir3-1PkkTRYZgbq9HX6BrFfc9tsHxEdXsU0qnE")
	public String acmeVerify2() {
		return "phHGlir3-1PkkTRYZgbq9HX6BrFfc9tsHxEdXsU0qnE.ozVtOSdqKRbxQDbQ2dmLVtob0bmuR46bYDMNtwmwlho";
	}
}
