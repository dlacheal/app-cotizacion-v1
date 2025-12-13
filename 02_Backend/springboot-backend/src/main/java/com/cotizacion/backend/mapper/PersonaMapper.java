package com.cotizacion.backend.mapper;

import com.cotizacion.backend.persistence.entity.Persona;
import com.cotizacion.backend.presentation.dto.request.PersonaRequestDTO;
import com.cotizacion.backend.presentation.dto.response.PersonaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PersonaMapper {

    PersonaResponseDTO toDto(Persona entity);

    @Mapping(target = "idPersona", ignore = true) // Generado por secuencia
    Persona toEntity(PersonaRequestDTO dto);
}
