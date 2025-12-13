package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.*;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonaServiceImpl implements PersonaService {
    private final PersonaRepository repository;

    @Override
    public Optional<Persona> findByDni(String dni) { return repository.findByDni(dni); }

    @Override
    public boolean existsByDni(String dni) { return repository.existsByDni(dni); }

    @Override
    @Transactional
    public Persona save(Persona persona) { return repository.save(persona); }
}
