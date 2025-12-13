package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.persistence.entity.Almacen;
// Importar tus DTOs y Mappers (Asumidos)
// import com.cotizacion.backend.presentation.dto.request.AlmacenRequestDTO;
// import com.cotizacion.backend.presentation.dto.response.AlmacenResponseDTO;
// import com.cotizacion.backend.mapper.AlmacenMapper;

import com.cotizacion.backend.service.interfaces.AlmacenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/almacenes")
@RequiredArgsConstructor
public class AlmacenController {

    private final AlmacenService almacenService;
    // private final AlmacenMapper mapper; // Inyectar mapper real

    @GetMapping
    public ResponseEntity<List<Almacen>> listarAlmacenes() {
        // Best Practice: Deberías devolver List<AlmacenResponseDTO> usando:
        // return ResponseEntity.ok(almacenService.findAll().stream().map(mapper::toDto).toList());
        return ResponseEntity.ok(almacenService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Almacen> buscarPorId(@PathVariable Long id) {
        return almacenService.findById(id)
                // .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Almacen> crearAlmacen(@RequestBody @Valid Almacen almacen) { // Usar DTO en prod
        // Almacen entity = mapper.toEntity(requestDTO);
        Almacen nuevoAlmacen = almacenService.save(almacen);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAlmacen);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Almacen> actualizarAlmacen(@PathVariable Long id, @RequestBody @Valid Almacen almacenDatos) {
        return almacenService.findById(id)
                .map(almacenExistente -> {
                    almacenExistente.setNombre(almacenDatos.getNombre());
                    almacenExistente.setDireccion(almacenDatos.getDireccion());
                    return ResponseEntity.ok(almacenService.save(almacenExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlmacen(@PathVariable Long id) {
        if (almacenService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        almacenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
