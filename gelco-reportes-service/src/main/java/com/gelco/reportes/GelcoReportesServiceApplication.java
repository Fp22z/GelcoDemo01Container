package com.gelco.reportes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GelcoReportesServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(GelcoReportesServiceApplication.class, args);
	}
}