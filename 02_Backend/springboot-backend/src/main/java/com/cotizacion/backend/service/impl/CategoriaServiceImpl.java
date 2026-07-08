package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.*;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.CategoriaService;
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
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository repository;

    @Override
    public List<Categoria> findAll() { return repository.findAll(); }

    @Override
    public Page<Categoria> findAll(Pageable pageable) { return repository.findAll(pageable); }

    @Override
    public Optional<Categoria> findById(Long id) { return repository.findById(id); }

    @Override
    @Transactional
    public Categoria save(Categoria categoria) { return repository.save(categoria); }

    @Override
    @Transactional
    public void delete(Long id) { repository.deleteById(id); }
}
