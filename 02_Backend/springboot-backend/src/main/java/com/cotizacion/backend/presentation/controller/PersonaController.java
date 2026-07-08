package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.persistence.entity.Persona;
import com.cotizacion.backend.presentation.dto.request.PersonaRequestDTO;
import com.cotizacion.backend.presentation.dto.response.PersonaResponseDTO;
import com.cotizacion.backend.mapper.PersonaMapper;
import com.cotizacion.backend.service.interfaces.PersonaService;
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
@RequestMapping("/api/v1/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;
    private final PersonaMapper mapper;

    @GetMapping
    public ResponseEntity<Page<PersonaResponseDTO>> listarPersonas(
            @PageableDefault(size = 10, sort = "idPersona") Pageable pageable) {
        Page<PersonaResponseDTO> personas = personaService.findAll(pageable)
                .map(mapper::toDto);
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<PersonaResponseDTO>> listarTodas() {
        List<PersonaResponseDTO> lista = personaService.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return personaService.findById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PersonaResponseDTO> buscarPorDni(@PathVariable String dni) {
        return personaService.findByDni(dni)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint utilitario para validaciones rápidas en frontend.
     * Retorna 200 con true/false.
     */
    @GetMapping("/validar-existencia/{dni}")
    public ResponseEntity<Boolean> existePorDni(@PathVariable String dni) {
        return ResponseEntity.ok(personaService.existsByDni(dni));
    }

    @PostMapping
    public ResponseEntity<?> crearPersona(@RequestBody @Valid PersonaRequestDTO requestDTO) {
        // Validación preventiva de negocio
        if (personaService.existsByDni(requestDTO.dni())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe una persona registrada con el DNI " + requestDTO.dni());
        }

        Persona entity = mapper.toEntity(requestDTO);
        Persona guardada = personaService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(guardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPersona(
            @PathVariable Integer id,
            @RequestBody @Valid PersonaRequestDTO requestDTO) {
        return personaService.findById(id)
                .map(personaExistente -> {
                    if (!personaExistente.getDni().equals(requestDTO.dni()) && personaService.existsByDni(requestDTO.dni())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("Ya existe otra persona registrada con el DNI " + requestDTO.dni());
                    }
                    
                    personaExistente.setDni(requestDTO.dni());
                    personaExistente.setNombre(requestDTO.nombre());
                    personaExistente.setTelefono(requestDTO.telefono());
                    personaExistente.setEmail(requestDTO.email());
                    
                    Persona actualizada = personaService.save(personaExistente);
                    return ResponseEntity.ok(mapper.toDto(actualizada));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPersona(@PathVariable Integer id) {
        if (personaService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        personaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Nota: Generalmente las Personas se actualizan a través del módulo de Usuario o Cliente,
    // pero si necesitas un PUT directo, sería similar al de Proveedor.
}
