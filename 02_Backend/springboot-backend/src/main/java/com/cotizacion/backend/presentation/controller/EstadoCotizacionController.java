package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.persistence.entity.EstadoCotizacion;
import com.cotizacion.backend.service.interfaces.EstadoCotizacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estados-cotizacion")
@RequiredArgsConstructor
public class EstadoCotizacionController {

    private final EstadoCotizacionService estadoService;

    @GetMapping
    public ResponseEntity<List<EstadoCotizacion>> listarEstados() {
        return ResponseEntity.ok(estadoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoCotizacion> buscarPorId(@PathVariable Long id) {
        return estadoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Búsqueda auxiliar por nombre (útil para validaciones frontend)
    @GetMapping("/buscar")
    public ResponseEntity<EstadoCotizacion> buscarPorNombre(@RequestParam String nombre) {
        return estadoService.findByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EstadoCotizacion> crearEstado(@RequestBody @Valid EstadoCotizacion estado) {
        // En sistemas reales, aquí verificaríamos roles de ADMIN
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoService.save(estado));
    }
}