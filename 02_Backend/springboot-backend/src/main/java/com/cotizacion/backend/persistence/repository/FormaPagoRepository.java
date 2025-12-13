package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.FormaPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormaPagoRepository extends JpaRepository<FormaPago, Long> {
    // Generalmente CRUD básico
}
