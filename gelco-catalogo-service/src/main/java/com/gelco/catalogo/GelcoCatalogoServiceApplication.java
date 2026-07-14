package com.gelco.catalogo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GelcoCatalogoServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(GelcoCatalogoServiceApplication.class, args);
	}
}