package com.gelco.reportes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "gelco-consultoras-service")
public interface ConsultorasClient {

    @GetMapping("/api/v1/consultoras")
    List<Map<String, Object>> getAllConsultoras();

    @GetMapping("/api/v1/ventas/consultora/{consultoraId}/mes/{mes}/anio/{anio}")
    Map<String, Object> getVentaPorMes(
            @PathVariable Long consultoraId,
            @PathVariable int mes,
            @PathVariable int anio);
}