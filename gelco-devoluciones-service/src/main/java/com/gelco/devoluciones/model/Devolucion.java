package com.gelco.devoluciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "devoluciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "detalle_pedido_id", nullable = false)
    private Long detallePedidoId;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "producto_nombre", length = 150)
    private String productoNombre;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Column(name = "cliente_nombre", length = 150)
    private String clienteNombre;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(length = 300)
    private String motivo;

    @Column(name = "condicion_producto", nullable = false, length = 20)
    private String condicionProducto;

    @Column(nullable = false, length = 30)
    private String estado = "Procesada";

    @Column(length = 500)
    private String observaciones;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "recepcionista_id", nullable = false)
    private Long recepcionistaId;

    @Column(name = "recepcionista_nombre", length = 100)
    private String recepcionistaNombre;
}