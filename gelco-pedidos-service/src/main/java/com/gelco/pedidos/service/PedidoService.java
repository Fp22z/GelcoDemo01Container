package com.gelco.pedidos.service;

import com.gelco.pedidos.client.CatalogoClient;
import com.gelco.pedidos.client.ConsultorasClient;
import com.gelco.pedidos.dto.ReponerStockRequest;
import com.gelco.pedidos.dto.CrearPedidoRequest;
import com.gelco.pedidos.dto.DetallePedidoDevolucionResponse;
import com.gelco.pedidos.dto.PedidoResponse;
import com.gelco.pedidos.model.*;
import com.gelco.pedidos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final CatalogoClient catalogoClient;
    private final ConsultorasClient consultorasClient;

    @Transactional(readOnly = true)
    public List<PedidoResponse> getAllPedidos() {
        return pedidoRepository.findAllWithRelations().stream()
                .map(p -> PedidoResponse.fromEntity(p, detallePedidoRepository.findByPedidoId(p.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> getPedidosByConsultora(Long consultoraId) {
        return pedidoRepository.findByConsultoraId(consultoraId).stream()
                .map(p -> PedidoResponse.fromEntity(p, detallePedidoRepository.findByPedidoId(p.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> getPedidosByCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(p -> PedidoResponse.fromEntity(p, detallePedidoRepository.findByPedidoId(p.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> getPedidosByEstado(String estado) {
        return pedidoRepository.findByEstado(estado).stream()
                .map(p -> PedidoResponse.fromEntity(p, detallePedidoRepository.findByPedidoId(p.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoResponse getPedidoById(Long id) {
        Pedido pedido = pedidoRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con id: " + id));
        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(id);
        return PedidoResponse.fromEntity(pedido, detalles);
    }

    @Transactional
    public PedidoResponse crearPedidoCompleto(Long usuarioId, CrearPedidoRequest request) {

        // 1. Resolver consultora vía Feign a Consultoras
        ConsultorasClient.ConsultoraBasicResponse consultora;
        try {
            consultora = consultorasClient.getConsultoraByUsuario(usuarioId);
        } catch (Exception e) {
            throw new IllegalArgumentException("No se encontró una consultora asociada al usuario autenticado");
        }

        // 2. Validar cliente
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cliente no encontrado con id: " + request.getClienteId()));

        // 3. Validar stock via Feign a Catálogo
        List<ItemValidado> itemsValidados = request.getItems().stream()
                .map(item -> {
                    Map<String, Object> productoInfo = catalogoClient.getProductoInfo(item.getProductoId());

                    boolean activo = Boolean.TRUE.equals(productoInfo.get("activo"));
                    if (!activo) {
                        throw new IllegalArgumentException(
                                "El producto con id " + item.getProductoId() + " no está disponible");
                    }

                    Integer stock = (Integer) productoInfo.get("stock");
                    if (stock == null || stock < item.getCantidad()) {
                        throw new IllegalArgumentException(
                                "Stock insuficiente para producto id " + item.getProductoId() +
                                        ". Disponible: " + stock + ", solicitado: " + item.getCantidad());
                    }

                    BigDecimal precio = new BigDecimal(productoInfo.get("precio").toString());
                    String nombre = (String) productoInfo.get("nombre");

                    return new ItemValidado(item.getProductoId(), nombre, precio, item.getCantidad());
                })
                .collect(Collectors.toList());

        // 4. Calcular total
        BigDecimal total = itemsValidados.stream()
                .map(i -> i.precio().multiply(BigDecimal.valueOf(i.cantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 5. Guardar pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setConsultoraId(consultora.id());
        pedido.setConsultoraNombre(consultora.usuarioNombre());
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("Creado");
        pedido.setTotal(total);
        pedidoRepository.save(pedido);

        // 6. Guardar detalles y descontar stock via Feign
        List<DetallePedido> detallesGuardados = itemsValidados.stream().map(item -> {
            catalogoClient.reponerStock(item.productoId(),
                    new ReponerStockRequest(-item.cantidad()));

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProductoId(item.productoId());
            detalle.setProductoNombre(item.nombre());
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(item.precio());
            return detallePedidoRepository.save(detalle);
        }).collect(Collectors.toList());

        return PedidoResponse.fromEntity(pedido, detallesGuardados);
    }

    @Transactional
    public PedidoResponse updatePedidoEstado(Long id, String estado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con id: " + id));

        List<String> estadosValidos = List.of(
                "Creado", "Enviado a Almacén", "En camino", "Entregado", "Cancelado");
        if (!estadosValidos.contains(estado)) {
            throw new IllegalArgumentException("Estado inválido. Valores permitidos: " + estadosValidos);
        }

        pedido.setEstado(estado);
        Pedido actualizado = pedidoRepository.save(pedido);
        return PedidoResponse.fromEntity(actualizado, detallePedidoRepository.findByPedidoId(id));
    }

    @Transactional
    public void deletePedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con id: " + id));
        pedidoRepository.delete(pedido);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getVentasAgrupadasPorProducto() {
        return detallePedidoRepository.findVentasAgrupadasPorProductoTodos().stream()
                .map(row -> {
                    Map<String, Object> m = new java.util.HashMap<>();
                    m.put("productoId", (Long) row[0]);
                    m.put("totalCantidad", ((Long) row[1]).intValue());
                    return m;
                })
                .collect(Collectors.toList());
    }

    public DetallePedidoDevolucionResponse getDetalleParaDevolucion(Long detallePedidoId) {
        DetallePedido detalle = detallePedidoRepository.findById(detallePedidoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Detalle de pedido no encontrado con id: " + detallePedidoId));

        return new DetallePedidoDevolucionResponse(
                detalle.getId(),
                detalle.getCantidad(),
                detalle.getProductoId(),
                detalle.getProductoNombre(),
                detalle.getPedido().getId(),
                detalle.getPedido().getEstado(),
                detalle.getPedido().getCliente().getNombre()
        );
    }

    private record ItemValidado(Long productoId, String nombre, BigDecimal precio, Integer cantidad) {}
}