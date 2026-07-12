package com.gelco.capacitacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GelcoCapacitacionServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(GelcoCapacitacionServiceApplication.class, args);
	}
}