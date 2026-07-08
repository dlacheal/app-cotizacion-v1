package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.*;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.AlmacenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlmacenServiceImpl implements AlmacenService {
    private final AlmacenRepository repository;

    @Override
    public List<Almacen> findAll() { return repository.findAll(); }

    @Override
    public Page<Almacen> findAll(Pageable pageable) { return repository.findAll(pageable); }

    @Override
    public Optional<Almacen> findById(Long id) { return repository.findById(id); }

    @Override
    @Transactional
    public Almacen save(Almacen almacen) { return repository.save(almacen); }

    @Override
    @Transactional
    public void delete(Long id) { repository.deleteById(id); }
}
