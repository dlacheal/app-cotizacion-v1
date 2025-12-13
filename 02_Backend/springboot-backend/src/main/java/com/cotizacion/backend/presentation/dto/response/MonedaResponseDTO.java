package com.cotizacion.backend.presentation.dto.response;

public record MonedaResponseDTO(
        Long idMoneda,
        String codigo,
        String descripcion
) {}
