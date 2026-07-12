package com.gelco.devoluciones.client;

import com.gelco.devoluciones.dto.ReponerStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gelco-catalogo-service")
public interface CatalogoClient {

    @PostMapping("/api/v1/productos/{id}/reponer-stock")
    void reponerStockPorDevolucion(@PathVariable Long id, @RequestBody ReponerStockRequest request);
}