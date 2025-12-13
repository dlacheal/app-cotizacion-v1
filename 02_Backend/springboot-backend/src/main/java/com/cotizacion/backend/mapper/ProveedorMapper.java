package com.cotizacion.backend.mapper;

import com.cotizacion.backend.persistence.entity.Proveedor;
import com.cotizacion.backend.presentation.dto.request.ProveedorRequestDTO;
import com.cotizacion.backend.presentation.dto.response.ProveedorResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ProveedorMapper {

    // Entity -> ResponseDTO
    ProveedorResponseDTO toDto(Proveedor entity);

    // RequestDTO -> Entity
    @Mapping(target = "idProveedor", ignore = true) // Generado por secuencia
    Proveedor toEntity(ProveedorRequestDTO dto);
}