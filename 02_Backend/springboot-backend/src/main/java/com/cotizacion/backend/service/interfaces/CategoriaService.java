package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> findAll();
    Page<Categoria> findAll(Pageable pageable);
    Optional<Categoria> findById(Long id);
    Categoria save(Categoria categoria);
    void delete(Long id);
}