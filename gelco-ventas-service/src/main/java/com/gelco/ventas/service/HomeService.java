package com.gelco.ventas.service;

import com.gelco.ventas.model.Consultora;
import com.gelco.ventas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final ConsultoraRepository consultoraRepository;
    private final VentaConsultoraRepository ventaConsultoraRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;

    public Map<String, Object> getPublicHome() {
        Map<String, Object> home = new HashMap<>();
        home.put("appName", "GELCO - Sistema de Gestión");
        home.put("version", "1.0.0");
        home.put("activeProducts", productoRepository.countByActivoTrue());
        home.put("totalClients", clienteRepository.count());
        return home;
    }

    public Map<String, Object> getConsultoraHome(Long usuarioId) {
        Map<String, Object> home = new HashMap<>();

        Consultora consultora = consultoraRepository.findByUsuarioId(usuarioId)
                .orElse(null);

        if (consultora == null) {
            home.put("error", "No se encontró perfil de consultora");
            return home;
        }

        Long consultoraId = consultora.getId();

        // Pedidos pendientes de la consultora
        long pedidosPendientes = pedidoRepository
                .countByConsultoraIdAndEstado(consultoraId, "Creado");

        // Ventas totales usando método existente
        BigDecimal ventasTotales = ventaConsultoraRepository
                .getTotalVentasByConsultora(consultoraId);

        // Clientes de la consultora
        long totalClientes = clienteRepository
                .findByConsultoraId(consultoraId).size();

        home.put("consultora", Map.of(
                "id", consultoraId,
                "nivel", consultora.getNivel() != null ? consultora.getNivel() : "Bronce",
                "ventasTotales", ventasTotales != null ? ventasTotales : BigDecimal.ZERO
        ));
        home.put("pedidosPendientes", pedidosPendientes);
        home.put("totalClientes", totalClientes);

        return home;
    }

    public Map<String, Object> getAdminHome() {
        Map<String, Object> home = new HashMap<>();
        home.put("totalProductos", productoRepository.count());
        home.put("totalConsultoras", consultoraRepository.count());
        home.put("totalClientes", clienteRepository.count());
        home.put("totalPedidos", pedidoRepository.count());
        return home;
    }

    public Map<String, Object> getDistribuidorHome() {
        Map<String, Object> home = new HashMap<>();
        home.put("totalPedidos", pedidoRepository.count());
        home.put("totalProductos", productoRepository.count());
        return home;
    }

    public Map<String, Object> getRrhhHome() {
        Map<String, Object> home = new HashMap<>();
        home.put("totalConsultoras", consultoraRepository.count());
        home.put("totalClientes", clienteRepository.count());
        return home;
    }
}