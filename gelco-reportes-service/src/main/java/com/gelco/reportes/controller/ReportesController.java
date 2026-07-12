package com.gelco.reportes.controller;

import com.gelco.reportes.service.ReportesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportesController {

    private final ReportesService reportesService;

    @GetMapping("/proyeccion")
    public ResponseEntity<?> getProyeccion() {
        try {
            return ResponseEntity.ok(reportesService.getProyeccionVentas());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al calcular proyección: " + e.getMessage());
        }
    }

    @GetMapping("/consultora/{id}")
    public ResponseEntity<?> getEstadisticasConsultora(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reportesService.getEstadisticasConsultora(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        try {
            return ResponseEntity.ok(reportesService.getDashboardGeneral());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al obtener dashboard: " + e.getMessage());
        }
    }
}