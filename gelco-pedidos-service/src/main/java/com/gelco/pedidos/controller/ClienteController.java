package com.gelco.pedidos.controller;

import com.gelco.pedidos.client.ConsultorasClient;
import com.gelco.pedidos.dto.ErrorResponse;
import com.gelco.pedidos.model.Cliente;
import com.gelco.pedidos.service.ClienteService;
import com.gelco.pedidos.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final JwtUtil jwtUtil;
    private final ConsultorasClient consultorasClient;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll(HttpServletRequest request) {
        Long consultoraId = getConsultoraIdFromToken(request);
        return ResponseEntity.ok(clienteService.getAllConStatsByConsultora(consultoraId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long consultoraId = getConsultoraIdFromToken(request);
            return ResponseEntity.ok(clienteService.getClienteConStats(id, consultoraId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Cliente no encontrado", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        try {
            Long consultoraId = getConsultoraIdFromToken(request);
            Cliente cliente = new Cliente();
            cliente.setNombre((String) payload.get("nombre"));
            cliente.setTelefono((String) payload.get("telefono"));
            cliente.setDireccion((String) payload.get("direccion"));
            cliente.setPreferencias((String) payload.get("preferencias"));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(clienteService.create(cliente, consultoraId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(400, "Error al crear cliente", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error al crear cliente", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Cliente cliente, HttpServletRequest request) {
        try {
            Long consultoraId = getConsultoraIdFromToken(request);
            return ResponseEntity.ok(clienteService.update(id, cliente, consultoraId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Cliente no encontrado", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error al actualizar cliente", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long consultoraId = getConsultoraIdFromToken(request);
            clienteService.delete(id, consultoraId);
            return ResponseEntity.ok(Map.of("mensaje", "Cliente eliminado correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Cliente no encontrado", e.getMessage()));
        }
    }

    private Long getConsultoraIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long usuarioId = jwtUtil.getUsuarioIdFromToken(token);
        try {
            return consultorasClient.getConsultoraByUsuario(usuarioId).id();
        } catch (Exception e) {
            throw new IllegalArgumentException("Consultora no encontrada para este usuario");
        }
    }
}