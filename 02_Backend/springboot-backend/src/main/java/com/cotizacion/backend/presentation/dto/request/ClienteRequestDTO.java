package com.cotizacion.backend.presentation.dto.request;

import jakarta.validation.constraints.*;

public record ClienteRequestDTO(
        @NotBlank(message = "El nombre del cliente es obligatorio")
        @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
        String nombre,

        @NotBlank(message = "El RUC o DNI es obligatorio")
        @Pattern(regexp = "\\d{8}|\\d{11}", message = "El documento debe ser un DNI (8) o RUC (11) válido")
        String rucDni,

        @Size(max = 30, message = "El teléfono no puede exceder 30 caracteres")
        String telefono,

        @Email(message = "El formato del correo no es válido")
        @Size(max = 150)
        String email,

        @Size(max = 500, message = "La dirección es muy larga")
        String direccion
) {}
