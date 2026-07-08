package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.mapper.ProductoMapper;
import com.cotizacion.backend.persistence.entity.Producto;
import com.cotizacion.backend.presentation.dto.request.ProductoRequestDTO;
import com.cotizacion.backend.presentation.dto.response.ProductoResponseDTO;
import com.cotizacion.backend.service.interfaces.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final ProductoMapper mapper;

    @GetMapping
    public ResponseEntity<Page<ProductoResponseDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "idProducto") Pageable pageable) {
        Page<ProductoResponseDTO> productos = productoService.findAll(pageable)
                .map(mapper::toDto);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProductoResponseDTO>> listarActivos() {
        List<ProductoResponseDTO> activos = productoService.findAllActivos()
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(activos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> buscarPorId(@PathVariable Long id) {
        return productoService.findById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@RequestBody @Valid ProductoRequestDTO requestDTO) {
        Producto productoEntity = mapper.toEntity(requestDTO);
        Producto productoGuardado = productoService.save(productoEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(productoGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable Long id,
            @RequestBody @Valid ProductoRequestDTO requestDTO) {
        return productoService.findById(id)
                .map(productoExistente -> {
                    mapper.updateEntityFromDto(requestDTO, productoExistente);
                    Producto productoActualizado = productoService.save(productoExistente);
                    return ResponseEntity.ok(mapper.toDto(productoActualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productoService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
