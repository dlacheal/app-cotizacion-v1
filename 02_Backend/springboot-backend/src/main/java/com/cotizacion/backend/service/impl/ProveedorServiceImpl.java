package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.*;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProveedorServiceImpl implements ProveedorService {
    private final ProveedorRepository repository;

    @Override
    public List<Proveedor> findAll() { return repository.findAll(); }

    @Override
    public Optional<Proveedor> findById(Long id) { return repository.findById(id); }

    @Override
    public List<Proveedor> buscarPorNombre(String termino) {
        return repository.findByNombreContainingIgnoreCase(termino);
    }

    @Override
    public Optional<Proveedor> buscarPorRuc(String ruc) { return repository.findByRuc(ruc); }

    @Override
    @Transactional
    public Proveedor save(Proveedor proveedor) { return repository.save(proveedor); }

    @Override
    @Transactional
    public void delete(Long id) { repository.deleteById(id); }
}
