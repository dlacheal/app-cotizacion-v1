package com.cotizacion.backend.mapper;

import com.cotizacion.backend.persistence.entity.Producto;
import com.cotizacion.backend.presentation.dto.request.ProductoRequestDTO;
import com.cotizacion.backend.presentation.dto.response.ProductoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ProductoMapper {

    // Entity -> DTO (Lectura)
    @Mapping(target = "categoriaNombre", source = "categoria.nombre")
    @Mapping(target = "proveedorNombre", source = "proveedor.nombre")
    ProductoResponseDTO toDto(Producto entity);

    // DTO -> Entity (Escritura)
    // MapStruct es inteligente: si el DTO tiene "idCategoria", y la Entity tiene "Categoria.idCategoria",
    // necesitamos decirle cómo mapearlo. La forma más limpia para FKs es:
    @Mapping(target = "categoria.idCategoria", source = "idCategoria")
    @Mapping(target = "proveedor.idProveedor", source = "idProveedor")
    @Mapping(target = "activo", constant = "true") // Por defecto al crear
    Producto toEntity(ProductoRequestDTO dto);
}
