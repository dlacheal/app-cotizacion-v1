package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.*;
import java.util.List;
import java.util.Optional;

public interface AlmacenService {
    List<Almacen> findAll();
    Optional<Almacen> findById(Long id);
    Almacen save(Almacen almacen);
    void delete(Long id);
}
