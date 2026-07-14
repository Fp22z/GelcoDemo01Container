package com.gelco.pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "gelco-consultoras-service")
public interface ConsultorasClient {

    record ConsultoraBasicResponse(Long id, Long usuarioId, String usuarioNombre, String nivel) {}

    @GetMapping("/api/v1/consultoras/usuario/{usuarioId}")
    ConsultoraBasicResponse getConsultoraByUsuario(@PathVariable Long usuarioId);

    @GetMapping("/api/v1/consultoras/{id}")
    ConsultoraBasicResponse getConsultoraById(@PathVariable Long id);

    @GetMapping("/api/v1/consultoras")
    List<ConsultoraBasicResponse> getAllConsultoras();
}