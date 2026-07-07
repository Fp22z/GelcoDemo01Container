package com.gelco.ops.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDevolucionResponse {
    private Long detallePedidoId;
    private Integer cantidad;
    private Long productoId;
    private String productoNombre;
    private Long pedidoId;
    private String pedidoEstado;
    private String clienteNombre;
}