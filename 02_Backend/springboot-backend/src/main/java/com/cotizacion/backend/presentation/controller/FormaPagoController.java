package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.persistence.entity.FormaPago;
import com.cotizacion.backend.service.interfaces.FormaPagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<FormaPago>> listarFormasPago(
            @PageableDefault(size = 10, sort = "idFormaPago") Pageable pageable) {
        return ResponseEntity.ok(formaPagoService.findAll(pageable));
    }

    @GetMapping("/lista")
    public ResponseEntity<List<FormaPago>> listarTodas() {
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

    @PutMapping("/{id}")
    public ResponseEntity<FormaPago> actualizarFormaPago(@PathVariable Long id, @RequestBody @Valid FormaPago formaPagoDatos) {
        return formaPagoService.findById(id)
                .map(formaPagoExistente -> {
                    formaPagoExistente.setNombre(formaPagoDatos.getNombre());
                    formaPagoExistente.setDescripcion(formaPagoDatos.getDescripcion());
                    return ResponseEntity.ok(formaPagoService.save(formaPagoExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFormaPago(@PathVariable Long id) {
        if (formaPagoService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        formaPagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
