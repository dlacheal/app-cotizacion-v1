package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.*;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.MonedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MonedaServiceImpl implements MonedaService {
    private final MonedaRepository repository;

    @Override
    public List<Moneda> findAll() { return repository.findAll(); }

    @Override
    public Optional<Moneda> findById(Long id) { return repository.findById(id); }

    @Override
    public Optional<Moneda> findByCodigo(String codigo) { return repository.findByCodigo(codigo); }

    @Override
    @Transactional
    public Moneda save(Moneda moneda) { return repository.save(moneda); }

    @Override
    @Transactional
    public void delete(Long id) { repository.deleteById(id); }
}
