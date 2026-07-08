package com.cotizacion.backend.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record CotizacionCreateDTO(
        @NotNull(message = "El cliente es obligatorio")
        Long idCliente,

        @NotNull(message = "La moneda es obligatoria")
        Long idMoneda,

        @NotNull(message = "La forma de pago es obligatoria")
        Long idFormaPago,

        Long idEstado,

        // Opcional: Si el usuario viene del token, este campo podría omitirse
        Integer idUsuario,

        @Min(value = 1, message = "La validez mínima es de 1 día")
        @Max(value = 90, message = "La validez máxima es de 90 días")
        Integer validezDias,

        String observaciones,

        @NotEmpty(message = "La cotización debe tener al menos un ítem")
        @Valid
        List<CotizacionDetalleCreateDTO> detalles
) {}
