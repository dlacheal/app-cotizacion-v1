package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.service.interfaces.MonedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/monedas")
@RequiredArgsConstructor
public class MonedaController {
    private final MonedaService monedaService;
    // private final MonedaMapper mapper;

    @GetMapping
    public ResponseEntity<List<?>> listarMonedas() {
        return ResponseEntity.ok(monedaService.findAll());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigo) {
        return monedaService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

