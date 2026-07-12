package com.gelco.devoluciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GelcoDevolucionesServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(GelcoDevolucionesServiceApplication.class, args);
	}
}