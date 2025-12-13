package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.Persona;
import java.util.Optional;

public interface PersonaService {
    // Servicio base para validaciones de identidad
    Optional<Persona> findByDni(String dni);
    boolean existsByDni(String dni);
    Persona save(Persona persona);
}
