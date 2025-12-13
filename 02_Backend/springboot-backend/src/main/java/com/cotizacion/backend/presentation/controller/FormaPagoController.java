package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.persistence.entity.FormaPago;
import com.cotizacion.backend.service.interfaces.FormaPagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/formas-pago")
@RequiredArgsConstructor
public class FormaPagoController {

    private final FormaPagoService formaPagoService;

    @GetMapping
    public ResponseEntity<List<FormaPago>> listarFormasPago() {
        return ResponseEntity.ok(formaPagoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormaPago> buscarPorId(@PathVariable Long id) {
        return formaPagoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FormaPago> crearFormaPago(@RequestBody @Valid FormaPago formaPago) {
        return ResponseEntity.status(HttpStatus.CREATED).body(formaPagoService.save(formaPago));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFormaPago(@PathVariable Long id) {
        // Verificamos existencia antes de borrar para devolver 404 si no existe
        if (formaPagoService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        formaPagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
