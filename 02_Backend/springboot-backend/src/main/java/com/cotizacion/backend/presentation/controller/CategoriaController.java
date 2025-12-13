package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.service.interfaces.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;
    // private final CategoriaMapper mapper; // Asumimos inyección del mapper

    @GetMapping
    public ResponseEntity<List<?>> listarCategorias() {
        // En un caso real, esto devolvería List<CategoriaResponseDTO>
        return ResponseEntity.ok(categoriaService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody @Valid Object requestDTO) {
        // Categoria entity = mapper.toEntity(requestDTO);
        // return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(categoriaService.save(entity)));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.save(null)); // Retorno temporal
    }
}
