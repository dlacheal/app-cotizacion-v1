package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.*;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.FormaPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FormaPagoServiceImpl implements FormaPagoService {
    private final FormaPagoRepository repository;

    @Override
    public List<FormaPago> findAll() { return repository.findAll(); }

    @Override
    public Optional<FormaPago> findById(Long id) { return repository.findById(id); }

    @Override
    @Transactional
    public FormaPago save(FormaPago formaPago) { return repository.save(formaPago); }

    @Override
    @Transactional
    public void delete(Long id) { repository.deleteById(id); }
}
