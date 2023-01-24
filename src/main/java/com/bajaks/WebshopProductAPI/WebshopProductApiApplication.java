package com.bajaks.WebshopProductAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class WebshopProductApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebshopProductApiApplication.class, args);
	}

}
