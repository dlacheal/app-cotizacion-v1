package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.Moneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonedaRepository extends JpaRepository<Moneda, Long> {
    // Vital para buscar por 'USD' o 'PEN'
    Optional<Moneda> findByCodigo(String codigo);
}
