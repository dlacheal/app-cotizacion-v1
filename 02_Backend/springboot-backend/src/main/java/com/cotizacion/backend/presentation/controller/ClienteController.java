package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.persistence.entity.Cliente;
import com.cotizacion.backend.presentation.dto.request.ClienteRequestDTO;
import com.cotizacion.backend.presentation.dto.response.ClienteResponseDTO;
import com.cotizacion.backend.mapper.ClienteMapper;
import com.cotizacion.backend.service.interfaces.ClienteService;
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
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper mapper;

    // ==================== READ (Listar Todos) ====================
    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> listarClientes(
            @PageableDefault(size = 10, sort = "idCliente") Pageable pageable) {
        Page<ClienteResponseDTO> clientes = clienteService.findAll(pageable)
                .map(mapper::toDto);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> lista = clienteService.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // ==================== READ (Buscar por ID) ====================
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarClientePorId(@PathVariable Long id) {
        return clienteService.findById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ==================== READ (Búsqueda Avanzada) ====================
    /**
     * Endpoint flexible: permite buscar por documento exacto o por coincidencia de nombre.
     * Ejemplo: GET /api/v1/clientes/buscar?doc=1045...
     * Ejemplo: GET /api/v1/clientes/buscar?nombre=Juan
     */
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(
            @RequestParam(required = false) String doc,
            @RequestParam(required = false) String nombre) {

        // 1. Prioridad: Búsqueda exacta por Documento
        if (doc != null && !doc.isBlank()) {
            return clienteService.buscarPorDocumento(doc)
                    .map(mapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        // 2. Búsqueda parcial por Nombre
        if (nombre != null && !nombre.isBlank()) {
            List<ClienteResponseDTO> resultados = clienteService.buscarPorNombre(nombre)
                    .stream()
                    .map(mapper::toDto)
                    .toList();
            return ResponseEntity.ok(resultados);
        }

        return ResponseEntity.badRequest().body("Debe enviar un parámetro de búsqueda: 'doc' o 'nombre'");
    }

    // ==================== CREATE ====================
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crearCliente(@RequestBody @Valid ClienteRequestDTO requestDTO) {
        // 1. Convertir DTO a Entidad
        Cliente clienteEntity = mapper.toEntity(requestDTO);

        // 2. Guardar en BD (Service maneja la transacción)
        Cliente clienteGuardado = clienteService.save(clienteEntity);

        // 3. Retornar DTO respuesta con 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(clienteGuardado));
    }

    // ==================== UPDATE ====================
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(
            @PathVariable Long id,
            @RequestBody @Valid ClienteRequestDTO requestDTO) {

        return clienteService.findById(id)
                .map(clienteExistente -> {
                    // Actualizamos los campos usando el mapper (más limpio que setear uno por uno)
                    mapper.updateEntityFromDto(requestDTO, clienteExistente);

                    // Guardamos los cambios
                    Cliente clienteActualizado = clienteService.save(clienteExistente);

                    return ResponseEntity.ok(mapper.toDto(clienteActualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ==================== DELETE ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        // Verificamos existencia antes de intentar borrar
        if (clienteService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
