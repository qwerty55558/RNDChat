package com.toyproject.apichat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@ServletComponentScan
public class ApichatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApichatApplication.class, args);
	}



}
