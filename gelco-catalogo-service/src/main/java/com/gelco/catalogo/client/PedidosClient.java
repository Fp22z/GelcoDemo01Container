package com.gelco.catalogo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "gelco-pedidos-service")
public interface PedidosClient {

    @GetMapping("/api/v1/pedidos/ventas-agrupadas-por-producto")
    List<Map<String, Object>> getVentasAgrupadasPorProducto();
}