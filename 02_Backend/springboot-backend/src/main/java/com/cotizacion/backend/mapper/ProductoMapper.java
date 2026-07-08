package com.cotizacion.backend.mapper;

import com.cotizacion.backend.persistence.entity.Categoria;
import com.cotizacion.backend.persistence.entity.Proveedor;
import com.cotizacion.backend.persistence.entity.Producto;
import com.cotizacion.backend.presentation.dto.request.ProductoRequestDTO;
import com.cotizacion.backend.presentation.dto.response.ProductoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ProductoMapper {

    // Entity -> DTO (Lectura)
    @Mapping(target = "categoriaNombre", source = "categoria.nombre")
    @Mapping(target = "proveedorNombre", source = "proveedor.nombre")
    ProductoResponseDTO toDto(Producto entity);

    // DTO -> Entity (Escritura)
    @Mapping(target = "categoria", source = "idCategoria")
    @Mapping(target = "proveedor", source = "idProveedor")
    @Mapping(target = "activo", constant = "true") // Por defecto al crear
    Producto toEntity(ProductoRequestDTO dto);

    // DTO -> Entity (Actualización)
    @Mapping(target = "idProducto", ignore = true)
    @Mapping(target = "categoria", source = "idCategoria")
    @Mapping(target = "proveedor", source = "idProveedor")
    void updateEntityFromDto(ProductoRequestDTO dto, @MappingTarget Producto entity);

    default Categoria mapCategoria(Long idCategoria) {
        if (idCategoria == null) return null;
        Categoria c = new Categoria();
        c.setIdCategoria(idCategoria);
        return c;
    }

    default Proveedor mapProveedor(Long idProveedor) {
        if (idProveedor == null) return null;
        Proveedor p = new Proveedor();
        p.setIdProveedor(idProveedor);
        return p;
    }
}
