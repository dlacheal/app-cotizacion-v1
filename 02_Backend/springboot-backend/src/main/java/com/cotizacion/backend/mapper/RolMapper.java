package com.cotizacion.backend.mapper;

import com.cotizacion.backend.persistence.entity.Rol;
import com.cotizacion.backend.presentation.dto.response.RolResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RolMapper {
    @Mapping(target = "rol", source = "nombre")
    RolResponseDTO toDto(Rol entity);
}
