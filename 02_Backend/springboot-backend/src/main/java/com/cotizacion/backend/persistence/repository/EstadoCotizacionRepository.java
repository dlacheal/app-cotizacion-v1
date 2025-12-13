package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.EstadoCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoCotizacionRepository extends JpaRepository<EstadoCotizacion, Long> {
    // Para cambiar estados por nombre lógico
    Optional<EstadoCotizacion> findByNombre(String nombre);
}
