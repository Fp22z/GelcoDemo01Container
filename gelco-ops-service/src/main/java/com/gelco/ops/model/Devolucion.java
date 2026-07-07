package com.gelco.ops.model;

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

    // Ya no es una relación JPA — Ops no tiene la tabla detalle_pedido.
    // Guardamos solo el id y un snapshot de los datos que Ventas devolvió.
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
    private String tipo; // "Cambio" | "Devolucion"

    @Column(length = 300)
    private String motivo;

    @Column(name = "condicion_producto", nullable = false, length = 20)
    private String condicionProducto; // "Apto" | "No apto"

    @Column(nullable = false, length = 30)
    private String estado = "Procesada";

    @Column(length = 500)
    private String observaciones;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    // Usuario sigue siendo relación JPA -- es modelo propio de Ops (copia local legítima)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recepcionista_id", nullable = false)
    private Usuario recepcionista;
}