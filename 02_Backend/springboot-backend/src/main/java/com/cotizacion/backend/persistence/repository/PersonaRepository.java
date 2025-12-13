package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    // Búsquedas clave por constraints únicos
    Optional<Persona> findByDni(String dni);
    Optional<Persona> findByEmail(String email);
    boolean existsByDni(String dni);
}
