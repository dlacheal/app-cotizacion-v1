package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.persistence.entity.Authority;
import com.cotizacion.backend.presentation.dto.request.AuthorityRequestDTO;
import com.cotizacion.backend.presentation.dto.response.AuthorityResponseDTO;
import com.cotizacion.backend.mapper.AuthorityMapper;
import com.cotizacion.backend.service.interfaces.AuthorityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authorities")
@RequiredArgsConstructor
public class AuthorityController {

    private final AuthorityService authorityService;
    private final AuthorityMapper mapper;

    /**
     * Obtener todos los roles de un usuario específico.
     * GET /api/v1/authorities/usuario/{idUsuario}
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<AuthorityResponseDTO>> listarRolesPorUsuario(@PathVariable Integer idUsuario) {
        List<AuthorityResponseDTO> roles = authorityService.findRolesByUsuario(idUsuario)
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(roles);
    }

    /**
     * Asignar un nuevo rol a un usuario.
     * POST /api/v1/authorities
     * Body: { "idUsuario": 1, "rol": "ROLE_ADMIN" }
     */
    @PostMapping
    public ResponseEntity<AuthorityResponseDTO> asignarRol(@RequestBody @Valid AuthorityRequestDTO requestDTO) {
        // El servicio se encarga de buscar el usuario y crear la entidad
        Authority nuevaAuthority = authorityService.addRoleToUsuario(
                requestDTO.idUsuario(),
                requestDTO.rol().toUpperCase() // Forzamos mayúsculas por convención
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(nuevaAuthority));
    }

    /**
     * Revocar un rol de un usuario.
     * DELETE /api/v1/authorities?idUsuario=1&rol=ROLE_ADMIN
     * Usamos Query Params porque borrar por ID de Authority es menos intuitivo para el cliente.
     */
    @DeleteMapping
    public ResponseEntity<Void> revocarRol(
            @RequestParam Integer idUsuario,
            @RequestParam String rol) {

        authorityService.removeRoleFromUsuario(idUsuario, rol.toUpperCase());
        return ResponseEntity.noContent().build();
    }
}
