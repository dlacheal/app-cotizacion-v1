package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.presentation.dto.request.ProductoRequestDTO;
import com.cotizacion.backend.service.interfaces.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @GetMapping("/activos")
    public ResponseEntity<List<?>> listarActivos() {
        return ResponseEntity.ok(productoService.findAllActivos());
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody @Valid ProductoRequestDTO requestDTO) {
        // Lógica de mapeo y guardado
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
