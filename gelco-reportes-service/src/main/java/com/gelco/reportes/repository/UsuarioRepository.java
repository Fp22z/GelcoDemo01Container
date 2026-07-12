package com.gelco.reportes.repository;

import com.gelco.reportes.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u JOIN u.perfil p WHERE p.nombre = :perfilNombre")
    List<Usuario> findByPerfilNombre(@Param("perfilNombre") String perfilNombre);
}
