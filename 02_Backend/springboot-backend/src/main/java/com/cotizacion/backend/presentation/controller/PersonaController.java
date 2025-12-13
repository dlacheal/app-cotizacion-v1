package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.persistence.entity.Persona;
import com.cotizacion.backend.presentation.dto.request.PersonaRequestDTO;
import com.cotizacion.backend.presentation.dto.response.PersonaResponseDTO;
import com.cotizacion.backend.mapper.PersonaMapper;
import com.cotizacion.backend.service.interfaces.PersonaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;
    private final PersonaMapper mapper;

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

    // Nota: Generalmente las Personas se actualizan a través del módulo de Usuario o Cliente,
    // pero si necesitas un PUT directo, sería similar al de Proveedor.
}
