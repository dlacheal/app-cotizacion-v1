package com.cotizacion.backend.mapper;

import com.cotizacion.backend.persistence.entity.Cliente;
import com.cotizacion.backend.presentation.dto.request.ClienteRequestDTO;
import com.cotizacion.backend.presentation.dto.response.ClienteResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ClienteMapper {

    // Entity -> ResponseDTO
    ClienteResponseDTO toDto(Cliente entity);

    // RequestDTO -> Entity (Creación)
    @Mapping(target = "idCliente", ignore = true)
    Cliente toEntity(ClienteRequestDTO dto);

    // RequestDTO -> Entity (Actualización)
    // Esto permite copiar propiedades del DTO a una entidad existente ignorando nulos si fuera necesario
    @Mapping(target = "idCliente", ignore = true)
    void updateEntityFromDto(ClienteRequestDTO dto, @MappingTarget Cliente entity);
}
