package com.gelco.devoluciones.client;

import com.gelco.devoluciones.dto.DetallePedidoDevolucionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gelco-pedidos-service")
public interface PedidosClient {

    @GetMapping("/api/v1/pedidos/detalle/{detallePedidoId}")
    DetallePedidoDevolucionResponse getDetalleParaDevolucion(@PathVariable Long detallePedidoId);
}