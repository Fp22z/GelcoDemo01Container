package com.gelco.pedidos.service;

import com.gelco.pedidos.client.ConsultorasClient;
import com.gelco.pedidos.repository.ClienteRepository;
import com.gelco.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ConsultorasClient consultorasClient;

    public Map<String, Object> getPublicHome() {
        Map<String, Object> home = new HashMap<>();
        home.put("appName", "GELCO - Sistema de Gestión");
        home.put("version", "1.0.0");
        home.put("totalClientes", clienteRepository.count());
        return home;
    }

    public Map<String, Object> getConsultoraHome(Long usuarioId) {
        Map<String, Object> home = new HashMap<>();
        ConsultorasClient.ConsultoraBasicResponse consultora;
        try {
            consultora = consultorasClient.getConsultoraByUsuario(usuarioId);
        } catch (Exception e) {
            home.put("error", "No se encontró perfil de consultora");
            return home;
        }
        long pedidosPendientes = pedidoRepository
                .countByConsultoraIdAndEstado(consultora.id(), "Creado");
        long totalClientes = clienteRepository
                .findByConsultoraId(consultora.id()).size();
        home.put("consultora", Map.of(
                "id", consultora.id(),
                "nivel", consultora.nivel() != null ? consultora.nivel() : "Bronce"
        ));
        home.put("pedidosPendientes", pedidosPendientes);
        home.put("totalClientes", totalClientes);
        return home;
    }

    public Map<String, Object> getAdminHome() {
        Map<String, Object> home = new HashMap<>();
        home.put("totalPedidos", pedidoRepository.count());
        home.put("totalClientes", clienteRepository.count());
        home.put("totalConsultoras", consultorasClient.getAllConsultoras().size());
        return home;
    }

    public Map<String, Object> getDistribuidorHome() {
        Map<String, Object> home = new HashMap<>();
        home.put("totalPedidos", pedidoRepository.count());
        return home;
    }

    public Map<String, Object> getRrhhHome() {
        Map<String, Object> home = new HashMap<>();
        home.put("totalConsultoras", consultorasClient.getAllConsultoras().size());
        home.put("totalClientes", clienteRepository.count());
        return home;
    }
}