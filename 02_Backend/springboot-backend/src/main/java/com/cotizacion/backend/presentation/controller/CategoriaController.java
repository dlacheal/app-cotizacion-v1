package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.persistence.entity.Categoria;
import com.cotizacion.backend.service.interfaces.CategoriaService;
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
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;
    // private final CategoriaMapper mapper; // Asumimos inyección del mapper

    @GetMapping
    public ResponseEntity<Page<Categoria>> listarCategorias(
            @PageableDefault(size = 10, sort = "idCategoria") Pageable pageable) {
        return ResponseEntity.ok(categoriaService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@RequestBody @Valid Categoria categoria) {
        Categoria nuevaCategoria = categoriaService.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Long id) {
        return categoriaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id,
            @RequestBody @Valid Categoria categoriaDatos) {
        return categoriaService.findById(id)
                .map(categoriaExistente -> {
                    categoriaExistente.setNombre(categoriaDatos.getNombre());
                    categoriaExistente.setDescripcion(categoriaDatos.getDescripcion());
                    return ResponseEntity.ok(categoriaService.save(categoriaExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        if (categoriaService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}