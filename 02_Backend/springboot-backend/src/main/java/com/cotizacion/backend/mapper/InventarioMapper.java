package com.cotizacion.backend.mapper;

import com.cotizacion.backend.persistence.entity.Inventario;
import com.cotizacion.backend.presentation.dto.response.InventarioResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface InventarioMapper {

    @Mapping(target = "productoNombre", source = "producto.nombre")
    @Mapping(target = "almacenNombre", source = "almacen.nombre")
    InventarioResponseDTO toDto(Inventario entity);
}
