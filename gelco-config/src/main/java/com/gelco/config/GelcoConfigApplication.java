package com.gelco.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class GelcoConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(GelcoConfigApplication.class, args);
	}

}