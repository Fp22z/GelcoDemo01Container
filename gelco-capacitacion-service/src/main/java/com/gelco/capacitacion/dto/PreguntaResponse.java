package com.gelco.capacitacion.dto;

import com.gelco.capacitacion.model.CapacitacionPregunta;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PreguntaResponse {

    private Long id;
    private String pregunta;
    private Integer orden;

    public static PreguntaResponse fromEntity(CapacitacionPregunta entity) {
        PreguntaResponse r = new PreguntaResponse();
        r.setId(entity.getId());
        r.setPregunta(entity.getPregunta());
        r.setOrden(entity.getOrden());
        return r;
    }
}
