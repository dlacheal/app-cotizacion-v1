package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface PersonaService {
    List<Persona> findAll();
    Page<Persona> findAll(Pageable pageable);
    Optional<Persona> findById(Integer id);
    Optional<Persona> findByDni(String dni);
    boolean existsByDni(String dni);
    Persona save(Persona persona);
    void delete(Integer id);
}
