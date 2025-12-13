package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.*;
import java.util.List;
import java.util.Optional;

public interface EstadoCotizacionService {
    List<EstadoCotizacion> findAll();
    Optional<EstadoCotizacion> findById(Long id);
    Optional<EstadoCotizacion> findByNombre(String nombre); // Para cambios de estado por lógica
    EstadoCotizacion save(EstadoCotizacion estado);
    // Nota: Generalmente no se borran estados, se inactivan, pero dejamos el delete por estandar
    void delete(Long id);
}