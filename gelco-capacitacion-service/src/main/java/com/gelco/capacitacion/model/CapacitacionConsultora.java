package com.gelco.capacitacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "capacitacion_consultora")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapacitacionConsultora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capacitacion_id", nullable = false)
    private Capacitacion capacitacion;

    @Column(name = "consultora_id", nullable = false)
    private Long consultoraId;

    @Column(name = "consultora_nombre", length = 100)
    private String consultoraNombre;

    @Column(name = "consultora_nivel", length = 100)
    private String consultoraNivel;

    @Column(nullable = false)
    private Boolean completado;

    @Column(precision = 5, scale = 2)
    private BigDecimal puntaje;
}