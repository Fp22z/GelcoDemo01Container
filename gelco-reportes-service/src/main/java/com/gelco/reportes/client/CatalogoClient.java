package com.gelco.reportes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "gelco-catalogo-service")
public interface CatalogoClient {

    @GetMapping("/api/v1/productos")
    List<Map<String, Object>> getAllProductos();
}