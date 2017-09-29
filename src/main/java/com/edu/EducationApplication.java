package com.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

//@SpringBootApplication
//public class EducationApplication extends SpringBootServletInitializer{
//
//	public static void main(String[] args) {
//		SpringApplication.run(EducationApplication.class, args);
//	}
//	
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(EducationApplication.class);
//    }
//}

@SpringBootApplication
public class EducationApplication{

	public static void main(String[] args) {
		SpringApplication.run(EducationApplication.class, args);
	}
}