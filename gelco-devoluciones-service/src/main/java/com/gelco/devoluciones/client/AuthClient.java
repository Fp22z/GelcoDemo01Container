package com.gelco.devoluciones.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gelco-auth-service")
public interface AuthClient {

    record UsuarioBasicResponse(Long id, String nombre, String email) {}

    @GetMapping("/api/v1/usuarios/{id}")
    UsuarioBasicResponse getUsuarioById(@PathVariable Long id);
}