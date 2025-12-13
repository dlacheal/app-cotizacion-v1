package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.*;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> findAll();
    Optional<Categoria> findById(Long id);
    Categoria save(Categoria categoria);
    void delete(Long id);
}