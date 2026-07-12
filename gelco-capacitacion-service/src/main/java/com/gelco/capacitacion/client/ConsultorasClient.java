package com.gelco.capacitacion.client;

import com.gelco.capacitacion.dto.VentaConsultoraDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gelco-consultoras-service")
public interface ConsultorasClient {

    @GetMapping("/api/v1/ventas/consultora/{consultoraId}/mes/{mes}/anio/{anio}")
    VentaConsultoraDTO getVentaPorMes(@PathVariable Long consultoraId, @PathVariable int mes, @PathVariable int anio);
}