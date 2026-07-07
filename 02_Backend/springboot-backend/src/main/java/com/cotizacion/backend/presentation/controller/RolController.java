package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.persistence.entity.Rol;
import com.cotizacion.backend.presentation.dto.request.RolRequestDTO;
import com.cotizacion.backend.presentation.dto.response.RolResponseDTO;
import com.cotizacion.backend.mapper.RolMapper;
import com.cotizacion.backend.service.interfaces.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authorities")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;
    private final RolMapper mapper;

    /**
     * Obtener todos los roles de un usuario específico.
     * GET /api/v1/roles/usuario/{idUsuario}
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<RolResponseDTO>> listarRolesPorUsuario(@PathVariable Integer idUsuario) {
        List<RolResponseDTO> roles = rolService.findRolesByUsuario(idUsuario)
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(roles);
    }

    /**
     * Asignar un nuevo rol a un usuario.
     * POST /api/v1/roles
     * Body: { "idUsuario": 1, "rol": "ROLE_ADMIN" }
     */
    @PostMapping
    public ResponseEntity<RolResponseDTO> asignarRol(@RequestBody @Valid RolRequestDTO requestDTO) {
        Rol nuevoRol = rolService.addRoleToUsuario(
                requestDTO.idUsuario(),
                requestDTO.rol().toUpperCase() // Forzamos mayúsculas por convención
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(nuevoRol));
    }

    /**
     * Revocar un rol de un usuario.
     * DELETE /api/v1/roles?idUsuario=1&rol=ROLE_ADMIN
     */
    @DeleteMapping
    public ResponseEntity<Void> revocarRol(
            @RequestParam Integer idUsuario,
            @RequestParam String rol) {

        rolService.removeRoleFromUsuario(idUsuario, rol.toUpperCase());
        return ResponseEntity.noContent().build();
    }
}
