package com.gelco.pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GelcoPedidosServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(GelcoPedidosServiceApplication.class, args);
	}
}