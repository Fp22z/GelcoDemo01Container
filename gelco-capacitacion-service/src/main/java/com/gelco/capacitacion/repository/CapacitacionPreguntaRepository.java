package com.gelco.capacitacion.repository;

import com.gelco.capacitacion.model.CapacitacionPregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapacitacionPreguntaRepository extends JpaRepository<CapacitacionPregunta, Long> {
    List<CapacitacionPregunta> findByCapacitacionIdOrderByOrdenAsc(Long capacitacionId);
    void deleteByCapacitacionId(Long capacitacionId);
}
