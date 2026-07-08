package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.FormaPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface FormaPagoService {
    List<FormaPago> findAll();
    Page<FormaPago> findAll(Pageable pageable);
    Optional<FormaPago> findById(Long id);
    FormaPago save(FormaPago formaPago);
    void delete(Long id);
}
