package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.Producto;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository repository;

    @Override
    public List<Producto> findAll() { return repository.findAll(); }

    @Override
    public List<Producto> findAllActivos() { return repository.findByActivoTrue(); }

    @Override
    public Optional<Producto> findById(Long id) { return repository.findById(id); }

    @Override
    public List<Producto> findByCategoria(Long idCategoria) { return repository.findByCategoriaIdCategoria(idCategoria); }

    @Override
    @Transactional
    public Producto save(Producto producto) { return repository.save(producto); }

    @Override
    @Transactional
    public void softDelete(Long id) {
        repository.findById(id).ifPresent(p -> {
            p.setActivo(false);
            repository.save(p);
        });
    }
}
