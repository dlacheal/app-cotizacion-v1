package com.cotizacion.backend.mapper;

import org.mapstruct.Builder;
import org.mapstruct.ReportingPolicy;

@org.mapstruct.MapperConfig(
        componentModel = "spring",            // Permite inyectar con @Autowired
        unmappedTargetPolicy = ReportingPolicy.IGNORE, // Evita errores por campos no mapeados (id, auditoría)
        builder = @Builder(disableBuilder = true) // Preferimos setters normales para JPA proxies
)
public interface MapperConfig {
}
