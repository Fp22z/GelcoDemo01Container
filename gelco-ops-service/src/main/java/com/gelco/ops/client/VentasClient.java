package com.gelco.ops.client;

import com.gelco.ops.dto.DetallePedidoDevolucionResponse;
import com.gelco.ops.dto.VentaConsultoraDTO;
import com.gelco.ops.dto.ReponerStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "gelco-ventas-service")
public interface VentasClient {

    @GetMapping("/api/v1/pedidos/detalle/{detallePedidoId}")
    DetallePedidoDevolucionResponse getDetalleParaDevolucion(@PathVariable Long detallePedidoId);

    @PostMapping("/api/v1/productos/{id}/reponer-stock")
    void reponerStockPorDevolucion(@PathVariable Long id, @RequestBody ReponerStockRequest request);

    @GetMapping("/api/v1/ventas/consultora/{consultoraId}/mes/{mes}/anio/{anio}")
    VentaConsultoraDTO getVentaPorMes(@PathVariable Long consultoraId, @PathVariable int mes, @PathVariable int anio);
}