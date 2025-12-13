package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.presentation.dto.request.UsuarioRegistroDTO; // DTO específico para registro
import com.cotizacion.backend.service.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth") // Endpoint más común para seguridad/login
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    // Asumimos inyección de PersonaService y UsuarioMapper

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody @Valid UsuarioRegistroDTO registroDTO) {
        // Lógica: 1. Crear/Buscar Persona 2. Crear Usuario 3. Encriptar Password
        // if (usuarioService.existsByUsername(registroDTO.username())) { ... BAD REQUEST }
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
    }

    // Nota: El login generalmente se maneja a través del filtro de Spring Security (ej: /login)
}
