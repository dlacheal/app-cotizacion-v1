package com.cotizacion.backend.mapper;

import com.cotizacion.backend.persistence.entity.Moneda;
import com.cotizacion.backend.presentation.dto.request.MonedaRequestDTO;
import com.cotizacion.backend.presentation.dto.response.MonedaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface MonedaMapper {

    // Entity -> DTO (Lectura)
    MonedaResponseDTO toDto(Moneda entity);

    // DTO -> Entity (Escritura)
    @Mapping(target = "idMoneda", ignore = true) // El ID se genera por secuencia en BD
    Moneda toEntity(MonedaRequestDTO dto);
}