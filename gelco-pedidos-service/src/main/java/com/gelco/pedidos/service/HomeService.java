package com.gelco.pedidos.service;

import com.gelco.pedidos.model.Consultora;
import com.gelco.pedidos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ConsultoraRepository consultoraRepository;

    public Map<String, Object> getPublicHome() {
        Map<String, Object> home = new HashMap<>();
        home.put("appName", "GELCO - Sistema de Gestión");
        home.put("version", "1.0.0");
        home.put("totalClientes", clienteRepository.count());
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
        long pedidosPendientes = pedidoRepository
                .countByConsultoraIdAndEstado(consultora.getId(), "Creado");
        long totalClientes = clienteRepository
                .findByConsultoraId(consultora.getId()).size();
        home.put("consultora", Map.of(
                "id", consultora.getId(),
                "nivel", consultora.getNivel() != null ? consultora.getNivel() : "Bronce"
        ));
        home.put("pedidosPendientes", pedidosPendientes);
        home.put("totalClientes", totalClientes);
        return home;
    }

    public Map<String, Object> getAdminHome() {
        Map<String, Object> home = new HashMap<>();
        home.put("totalPedidos", pedidoRepository.count());
        home.put("totalClientes", clienteRepository.count());
        home.put("totalConsultoras", consultoraRepository.count());
        return home;
    }

    public Map<String, Object> getDistribuidorHome() {
        Map<String, Object> home = new HashMap<>();
        home.put("totalPedidos", pedidoRepository.count());
        return home;
    }

    public Map<String, Object> getRrhhHome() {
        Map<String, Object> home = new HashMap<>();
        home.put("totalConsultoras", consultoraRepository.count());
        home.put("totalClientes", clienteRepository.count());
        return home;
    }
}