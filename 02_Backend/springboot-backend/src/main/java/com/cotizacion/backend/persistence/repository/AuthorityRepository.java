package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    // Usualmente se accede via Usuario.getAuthorities(), pero útil si gestionas roles por separado
    List<Authority> findByUsuarioIdUsuario(Integer idUsuario);
}