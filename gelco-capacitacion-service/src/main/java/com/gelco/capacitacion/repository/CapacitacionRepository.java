package com.gelco.capacitacion.repository;

import com.gelco.capacitacion.model.Capacitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapacitacionRepository extends JpaRepository<Capacitacion, Long> {
}
