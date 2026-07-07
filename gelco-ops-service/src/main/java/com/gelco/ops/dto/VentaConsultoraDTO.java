package com.gelco.ops.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// El endpoint de Ventas devuelve un Map con más campos ("ventas": lista detallada)
// que no necesitamos. @JsonIgnoreProperties evita que Feign/Jackson falle por eso.
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VentaConsultoraDTO {
    private Long consultoraId;
    private Integer mes;
    private Integer anio;
    private BigDecimal totalVentas;
    private Integer registros;
}