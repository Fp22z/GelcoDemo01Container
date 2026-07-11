// DTO nuevo: DetallePedidoDevolucionResponse.java (Ventas)
package com.gelco.catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
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