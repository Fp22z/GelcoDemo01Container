package com.gelco.devoluciones.controller;

import com.gelco.devoluciones.dto.CrearDevolucionRequest;
import com.gelco.devoluciones.dto.DevolucionResponse;
import com.gelco.devoluciones.dto.ErrorResponse;
import com.gelco.devoluciones.service.DevolucionService;
import com.gelco.devoluciones.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/devoluciones")
@RequiredArgsConstructor
public class DevolucionController {

    private final DevolucionService devolucionService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getAllDevoluciones() {
        try {
            return ResponseEntity.ok(devolucionService.getAllDevoluciones());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error al obtener devoluciones", e.getMessage()));
        }
    }

    @GetMapping("/detalle/{detallePedidoId}")
    public ResponseEntity<?> getDevolucionesByDetalle(@PathVariable Integer detallePedidoId) {
        try {
            return ResponseEntity.ok(devolucionService.getDevolucionesByDetalle(detallePedidoId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error al obtener devoluciones", e.getMessage()));
        }
    }

    @GetMapping("/detalle/{detallePedidoId}/disponible")
    public ResponseEntity<?> getCantidadDisponible(@PathVariable Integer detallePedidoId) {
        try {
            Integer disponible = devolucionService.getCantidadDisponibleParaDevolucion(detallePedidoId);
            return ResponseEntity.ok(Map.of("disponible", disponible));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Detalle no encontrado", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error al calcular disponible", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('RECEPCIONISTA')")
    public ResponseEntity<?> crearDevolucion(
            @Valid @RequestBody CrearDevolucionRequest request,
            HttpServletRequest httpRequest) {
        try {
            Long usuarioId = getUsuarioIdFromJwt(httpRequest);
            DevolucionResponse devolucion = devolucionService.crearDevolucion(usuarioId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(devolucion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(400, "Error al registrar devolución", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Error interno", e.getMessage()));
        }
    }

    private Long getUsuarioIdFromJwt(HttpServletRequest request) {
        String token = extraerToken(request);
        Long usuarioId = jwtUtil.getUsuarioIdFromToken(token);
        if (usuarioId == null) throw new IllegalArgumentException("No se pudo obtener el usuario del token");
        return usuarioId;
    }

    private String extraerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token de autorización no proporcionado");
        }
        return header.substring(7);
    }
}