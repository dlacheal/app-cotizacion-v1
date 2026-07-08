package com.cotizacion.backend.presentation.dto.request;

import jakarta.validation.constraints.*;

public record UsuarioUpdateDTO(
        @NotBlank(message = "El username es obligatorio")
        @Size(min = 4, max = 50, message = "El username debe tener entre 4 y 50 caracteres")
        String username,

        // Opcional al actualizar
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password,

        @NotNull(message = "Debes vincular el usuario a una Persona existente")
        Integer idPersona,

        Boolean enable,

        String[] roles
) {}
