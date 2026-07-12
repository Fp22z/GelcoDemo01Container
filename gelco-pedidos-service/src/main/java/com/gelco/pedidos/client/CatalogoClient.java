package com.gelco.pedidos.client;

import com.gelco.pedidos.dto.ReponerStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "gelco-catalogo-service")
public interface CatalogoClient {

    @GetMapping("/api/v1/productos/{id}")
    Map<String, Object> getProductoInfo(@PathVariable Long id);

    @PostMapping("/api/v1/productos/{id}/reponer-stock")
    void reponerStock(@PathVariable Long id, @RequestBody ReponerStockRequest request);
}