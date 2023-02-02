package com.bajaks.RisWebshopApi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Webshop API", version = "1.0"))
public class RisWebshopApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RisWebshopApiApplication.class, args);
	}

}
