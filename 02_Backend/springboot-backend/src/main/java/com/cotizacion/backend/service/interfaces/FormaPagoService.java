package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.*;
import java.util.List;
import java.util.Optional;

public interface FormaPagoService {
    List<FormaPago> findAll();
    Optional<FormaPago> findById(Long id);
    FormaPago save(FormaPago formaPago);
    void delete(Long id);
}
