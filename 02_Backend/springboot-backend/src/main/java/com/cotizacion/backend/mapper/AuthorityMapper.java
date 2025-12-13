package com.cotizacion.backend.mapper;

import com.cotizacion.backend.persistence.entity.Authority;
import com.cotizacion.backend.presentation.dto.response.AuthorityResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AuthorityMapper {

    @Mapping(target = "rol", source = "tipo") // En BD es 'tipo', en API es 'rol'
    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    @Mapping(target = "username", source = "usuario.username")
    AuthorityResponseDTO toDto(Authority entity);
}
