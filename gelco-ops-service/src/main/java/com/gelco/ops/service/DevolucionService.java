package com.gelco.ops.service;

import com.gelco.ops.client.VentasClient;
import com.gelco.ops.dto.CrearDevolucionRequest;
import com.gelco.ops.dto.DetallePedidoDevolucionResponse;
import com.gelco.ops.dto.DevolucionResponse;
import com.gelco.ops.dto.ReponerStockRequest;
import com.gelco.ops.model.Devolucion;
import com.gelco.ops.model.Usuario;
import com.gelco.ops.repository.DevolucionRepository;
import com.gelco.ops.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DevolucionService {

    private static final List<String> TIPOS_VALIDOS = List.of("Cambio", "Devolucion");
    private static final List<String> CONDICIONES_VALIDAS = List.of("Apto", "No apto");

    private final DevolucionRepository devolucionRepository;
    private final UsuarioRepository usuarioRepository;
    private final VentasClient ventasClient;

    @Transactional
    public DevolucionResponse crearDevolucion(Long usuarioId, CrearDevolucionRequest request) {

        if (!TIPOS_VALIDOS.contains(request.getTipo())) {
            throw new IllegalArgumentException("Tipo inválido. Valores permitidos: " + TIPOS_VALIDOS);
        }
        if (!CONDICIONES_VALIDAS.contains(request.getCondicionProducto())) {
            throw new IllegalArgumentException("Condición inválida. Valores permitidos: " + CONDICIONES_VALIDAS);
        }

        DetallePedidoDevolucionResponse detalle;
        try {
            detalle = ventasClient.getDetalleParaDevolucion(request.getDetallePedidoId());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Detalle de pedido no encontrado con id: " + request.getDetallePedidoId());
        }

        if (!"Entregado".equals(detalle.getPedidoEstado())) {
            throw new IllegalArgumentException(
                    "Solo se pueden procesar devoluciones de pedidos en estado 'Entregado'. Estado actual: "
                            + detalle.getPedidoEstado());
        }

        Integer yaDevuelto = devolucionRepository.sumCantidadDevueltaByDetalleId(request.getDetallePedidoId());
        int disponible = detalle.getCantidad() - (yaDevuelto != null ? yaDevuelto : 0);
        if (request.getCantidad() > disponible) {
            throw new IllegalArgumentException(
                    "Cantidad solicitada excede lo disponible para devolver. Disponible: " + disponible +
                            ", solicitado: " + request.getCantidad());
        }

        Usuario recepcionista = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + usuarioId));

        if ("Apto".equals(request.getCondicionProducto())) {
            ReponerStockRequest reponerRequest = new ReponerStockRequest();
            reponerRequest.setCantidad(request.getCantidad());
            ventasClient.reponerStockPorDevolucion(detalle.getProductoId(), reponerRequest);
        }

        Devolucion devolucion = new Devolucion();
        devolucion.setDetallePedidoId(detalle.getDetallePedidoId());
        devolucion.setProductoId(detalle.getProductoId());
        devolucion.setProductoNombre(detalle.getProductoNombre());
        devolucion.setPedidoId(detalle.getPedidoId());
        devolucion.setClienteNombre(detalle.getClienteNombre());
        devolucion.setCantidad(request.getCantidad());
        devolucion.setTipo(request.getTipo());
        devolucion.setMotivo(request.getMotivo());
        devolucion.setCondicionProducto(request.getCondicionProducto());
        devolucion.setEstado("Procesada");
        devolucion.setObservaciones(request.getObservaciones());
        devolucion.setFechaSolicitud(LocalDateTime.now());
        devolucion.setRecepcionista(recepcionista);

        Devolucion guardada = devolucionRepository.save(devolucion);

        Devolucion conRelaciones = devolucionRepository.findByIdWithRelations(guardada.getId().longValue())
                .orElseThrow(() -> new IllegalStateException("Error al recuperar la devolución guardada"));
        return DevolucionResponse.fromEntity(conRelaciones);
    }

    @Transactional(readOnly = true)
    public List<DevolucionResponse> getAllDevoluciones() {
        return devolucionRepository.findAllWithRelations().stream()
                .map(DevolucionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DevolucionResponse> getDevolucionesByDetalle(Integer detallePedidoId) {
        return devolucionRepository.findByDetallePedidoId(detallePedidoId.longValue()).stream()
                .map(DevolucionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Integer getCantidadDisponibleParaDevolucion(Integer detallePedidoId) {
        DetallePedidoDevolucionResponse detalle;
        try {
            detalle = ventasClient.getDetalleParaDevolucion(detallePedidoId.longValue());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Detalle de pedido no encontrado con id: " + detallePedidoId);
        }
        Integer yaDevuelto = devolucionRepository.sumCantidadDevueltaByDetalleId(detallePedidoId.longValue());
        return detalle.getCantidad() - (yaDevuelto != null ? yaDevuelto : 0);
    }
}