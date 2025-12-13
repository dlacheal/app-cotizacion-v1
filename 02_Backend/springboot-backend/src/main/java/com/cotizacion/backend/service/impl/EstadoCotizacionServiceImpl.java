package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.*;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.EstadoCotizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class EstadoCotizacionServiceImpl implements EstadoCotizacionService {
    private final EstadoCotizacionRepository repository;

    @Override
    public List<EstadoCotizacion> findAll() { return repository.findAll(); }

    @Override
    public Optional<EstadoCotizacion> findById(Long id) { return repository.findById(id); }

    @Override
    public Optional<EstadoCotizacion> findByNombre(String nombre) { return repository.findByNombre(nombre); }

    @Override
    @Transactional
    public EstadoCotizacion save(EstadoCotizacion estado) { return repository.save(estado); }

    @Override
    @Transactional
    public void delete(Long id) { repository.deleteById(id); }
}
