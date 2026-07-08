package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.persistence.entity.Proveedor;
import com.cotizacion.backend.presentation.dto.request.ProveedorRequestDTO;
import com.cotizacion.backend.presentation.dto.response.ProveedorResponseDTO;
import com.cotizacion.backend.mapper.ProveedorMapper;
import com.cotizacion.backend.service.interfaces.ProveedorService;
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
@RequestMapping("/api/v1/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService proveedorService;
    private final ProveedorMapper mapper;

    @GetMapping
    public ResponseEntity<Page<ProveedorResponseDTO>> listarProveedores(
            @PageableDefault(size = 10, sort = "idProveedor") Pageable pageable) {
        Page<ProveedorResponseDTO> pagina = proveedorService.findAll(pageable)
                .map(mapper::toDto);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> buscarPorId(@PathVariable Long id) {
        return proveedorService.findById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint de búsqueda flexible:
     * - Si envías ?ruc=20123... busca exacto por RUC.
     * - Si envías ?nombre=Distribuidora... busca coincidencias parciales.
     */
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(
            @RequestParam(required = false) String ruc,
            @RequestParam(required = false) String nombre) {

        if (ruc != null && !ruc.isBlank()) {
            return proveedorService.buscarPorRuc(ruc)
                    .map(mapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        if (nombre != null && !nombre.isBlank()) {
            List<ProveedorResponseDTO> resultados = proveedorService.buscarPorNombre(nombre)
                    .stream()
                    .map(mapper::toDto)
                    .toList();
            return ResponseEntity.ok(resultados);
        }

        return ResponseEntity.badRequest().body("Debe especificar un criterio de búsqueda (ruc o nombre)");
    }

    @PostMapping
    public ResponseEntity<ProveedorResponseDTO> crearProveedor(@RequestBody @Valid ProveedorRequestDTO requestDTO) {
        // 1. Convertir DTO a Entity
        Proveedor entity = mapper.toEntity(requestDTO);

        // 2. Guardar (El servicio maneja la tx)
        Proveedor guardado = proveedorService.save(entity);

        // 3. Devolver DTO respuesta
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> actualizarProveedor(
            @PathVariable Long id,
            @RequestBody @Valid ProveedorRequestDTO requestDTO) {

        return proveedorService.findById(id)
                .map(proveedorExistente -> {
                    // Mapeo manual o usar un método @MappingTarget en el mapper para actualizar
                    proveedorExistente.setNombre(requestDTO.nombre());
                    proveedorExistente.setRuc(requestDTO.ruc());
                    proveedorExistente.setTelefono(requestDTO.telefono());
                    proveedorExistente.setEmail(requestDTO.email());
                    proveedorExistente.setDireccion(requestDTO.direccion());

                    Proveedor actualizado = proveedorService.save(proveedorExistente);
                    return ResponseEntity.ok(mapper.toDto(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable Long id) {
        if (proveedorService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        proveedorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
