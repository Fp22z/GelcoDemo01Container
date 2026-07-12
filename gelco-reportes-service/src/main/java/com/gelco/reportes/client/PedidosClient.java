package com.gelco.reportes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "gelco-pedidos-service")
public interface PedidosClient {

    @GetMapping("/api/v1/pedidos")
    List<Map<String, Object>> getAllPedidos();

    @GetMapping("/api/v1/pedidos/consultora/{consultoraId}")
    List<Map<String, Object>> getPedidosByConsultora(@PathVariable Long consultoraId);

    @GetMapping("/api/v1/clientes")
    List<Map<String, Object>> getAllClientes();
}