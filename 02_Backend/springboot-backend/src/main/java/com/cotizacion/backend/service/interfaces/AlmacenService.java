package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.Almacen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface AlmacenService {
    List<Almacen> findAll();
    Page<Almacen> findAll(Pageable pageable);
    Optional<Almacen> findById(Long id);
    Almacen save(Almacen almacen);
    void delete(Long id);
}
