package com.cotizacion.backend.presentation.dto.response;

public record AuthorityResponseDTO(
        Integer idAuthority,
        String rol,
        Integer idUsuario,
        String username // Útil para confirmar visualmente a quién pertenece
) {}