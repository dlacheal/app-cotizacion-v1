package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.Moneda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface MonedaService {
    List<Moneda> findAll();
    Page<Moneda> findAll(Pageable pageable);
    Optional<Moneda> findById(Long id);
    Optional<Moneda> findByCodigo(String codigo); // Vital para conversiones
    Moneda save(Moneda moneda);
    void delete(Long id);
}
