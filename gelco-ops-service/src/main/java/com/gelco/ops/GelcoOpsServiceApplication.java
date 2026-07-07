package com.gelco.ops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GelcoOpsServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(GelcoOpsServiceApplication.class, args);
	}
}