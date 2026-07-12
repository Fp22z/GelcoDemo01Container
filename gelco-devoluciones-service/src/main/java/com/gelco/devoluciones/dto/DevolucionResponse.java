package com.gelco.devoluciones.dto;

import com.gelco.devoluciones.model.Devolucion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucionResponse {

    private Integer id;
    private Integer detallePedidoId;
    private Long pedidoId;
    private String clienteNombre;
    private Integer productoId;
    private String productoNombre;
    private Integer cantidad;
    private String tipo;
    private String motivo;
    private String condicionProducto;
    private String estado;
    private String observaciones;
    private LocalDateTime fechaSolicitud;
    private String recepcionistaNombre;

    public static DevolucionResponse fromEntity(Devolucion d) {
        DevolucionResponse r = new DevolucionResponse();
        r.setId(d.getId());
        r.setDetallePedidoId(d.getDetallePedidoId().intValue());
        r.setPedidoId(d.getPedidoId());
        r.setClienteNombre(d.getClienteNombre());
        r.setProductoId(d.getProductoId().intValue());
        r.setProductoNombre(d.getProductoNombre());
        r.setCantidad(d.getCantidad());
        r.setTipo(d.getTipo());
        r.setMotivo(d.getMotivo());
        r.setCondicionProducto(d.getCondicionProducto());
        r.setEstado(d.getEstado());
        r.setObservaciones(d.getObservaciones());
        r.setFechaSolicitud(d.getFechaSolicitud());
        r.setRecepcionistaNombre(d.getRecepcionista().getNombre());
        return r;
    }
}