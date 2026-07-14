package com.gelco.pedidos.service;

import com.gelco.pedidos.client.ConsultorasClient;
import com.gelco.pedidos.dto.OrdenCompraResponse;
import com.gelco.pedidos.model.Factura;
import com.gelco.pedidos.model.OrdenCompra;
import com.gelco.pedidos.model.Pedido;
import com.gelco.pedidos.repository.FacturaRepository;
import com.gelco.pedidos.repository.OrdenCompraRepository;
import com.gelco.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final FacturaRepository facturaRepository;
    private final PedidoRepository pedidoRepository;
    private final ConsultorasClient consultorasClient;

    private Long resolverConsultoraId(Long usuarioId) {
        try {
            return consultorasClient.getConsultoraByUsuario(usuarioId).id();
        } catch (Exception e) {
            throw new IllegalArgumentException("Consultora no encontrada");
        }
    }

    public List<OrdenCompraResponse> getPedidosDisponibles(Long usuarioId) {
        Long consultoraId = resolverConsultoraId(usuarioId);

        return pedidoRepository.findByConsultoraId(consultoraId)
                .stream()
                .filter(p -> p.getEstado().equals("Creado"))
                .map(p -> toResponse(p, false, null, null))
                .toList();
    }

    public List<OrdenCompraResponse> getMisOrdenes(Long usuarioId) {
        Long consultoraId = resolverConsultoraId(usuarioId);

        return ordenCompraRepository.findByPedidoConsultoraId(consultoraId)
                .stream()
                .map(oc -> {
                    Factura factura = facturaRepository
                            .findByPedidoConsultoraId(consultoraId)
                            .stream()
                            .filter(f -> f.getPedido().getId().equals(oc.getPedido().getId()))
                            .findFirst()
                            .orElse(null);
                    return toResponse(oc.getPedido(), true, oc, factura);
                })
                .toList();
    }

    @Transactional
    public List<OrdenCompraResponse> generarOrdenes(Long usuarioId, List<Long> pedidoIds) {
        Long consultoraId = resolverConsultoraId(usuarioId);

        return pedidoIds.stream().map(pedidoId -> {

            Pedido pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + pedidoId));

            if (!pedido.getConsultoraId().equals(consultoraId)) {
                throw new IllegalArgumentException("El pedido no pertenece a esta consultora");
            }

            if (ordenCompraRepository.existsByPedidoId(pedidoId)) {
                throw new IllegalArgumentException("El pedido #" + pedidoId + " ya tiene una orden de compra");
            }

            if (!pedido.getEstado().equals("Creado")) {
                throw new IllegalArgumentException("El pedido #" + pedidoId + " no está en estado Creado");
            }

            pedido.setEstado("Enviado a Almacén");
            pedidoRepository.save(pedido);

            OrdenCompra oc = new OrdenCompra();
            oc.setPedido(pedido);
            oc.setFecha(LocalDateTime.now());
            oc.setTotal(pedido.getTotal());
            ordenCompraRepository.save(oc);

            Factura factura = new Factura();
            factura.setPedido(pedido);
            factura.setTotal(pedido.getTotal());
            factura.setFecha(LocalDateTime.now());
            factura.setEstado("Pendiente de Pago");
            facturaRepository.save(factura);

            return toResponse(pedido, true, oc, factura);

        }).toList();
    }

    private OrdenCompraResponse toResponse(
            Pedido pedido,
            boolean tieneOrden,
            OrdenCompra oc,
            Factura factura) {

        return new OrdenCompraResponse(
                oc != null ? oc.getId() : null,
                pedido.getId(),
                pedido.getCliente() != null ? pedido.getCliente().getNombre() : "Sin cliente",
                oc != null ? oc.getFecha() : pedido.getFecha(),
                pedido.getTotal(),
                factura != null ? factura.getEstado() : null,
                factura != null ? factura.getId() : null,
                tieneOrden
        );
    }
}