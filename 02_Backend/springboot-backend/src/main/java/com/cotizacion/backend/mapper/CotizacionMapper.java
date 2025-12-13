package com.cotizacion.backend.mapper;

import com.cotizacion.backend.persistence.entity.Cotizacion;
import com.cotizacion.backend.persistence.entity.CotizacionDetalle;
import com.cotizacion.backend.presentation.dto.request.CotizacionCreateDTO;
import com.cotizacion.backend.presentation.dto.request.CotizacionDetalleCreateDTO;
import com.cotizacion.backend.presentation.dto.response.CotizacionResponseDTO;
import com.cotizacion.backend.presentation.dto.response.CotizacionDetalleResponseDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CotizacionMapper {

    // 1. LECTURA (Entity -> ResponseDTO)
    @Mapping(target = "clienteNombre", source = "cliente.nombre")
    @Mapping(target = "clienteRucDni", source = "cliente.rucDni")
    @Mapping(target = "usuarioUsername", source = "usuario.username")
    @Mapping(target = "monedaCodigo", source = "moneda.codigo")
    @Mapping(target = "monedaDescripcion", source = "moneda.descripcion")
    @Mapping(target = "formaPagoNombre", source = "formaPago.nombre")
    @Mapping(target = "estadoNombre", source = "estado.nombre")
    CotizacionResponseDTO toDto(Cotizacion entity);

    @Mapping(target = "productoNombre", source = "producto.nombre")
    @Mapping(target = "idProducto", source = "producto.idProducto")
        // Nota: Si agregaste Proveedor al DTO de respuesta, mapéalo aquí también
    CotizacionDetalleResponseDTO toDetalleDto(CotizacionDetalle entity);


    // 2. ESCRITURA (RequestDTO -> Entity)

    // Ignoramos IDs y campos calculados que gestiona el Service
    @Mapping(target = "idCotizacion", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "estado", ignore = true)

    // Nuevos campos financieros calculados (Ignorar al crear desde DTO)
    @Mapping(target = "inversionTotal", ignore = true)
    @Mapping(target = "utilidadBrutaTotal", ignore = true)
    @Mapping(target = "subTotal", ignore = true)
    @Mapping(target = "montoIgv", ignore = true)
    @Mapping(target = "totalGeneral", ignore = true)

    // Mapeos de relaciones
    @Mapping(target = "cliente.idCliente", source = "idCliente")
    @Mapping(target = "moneda.idMoneda", source = "idMoneda")
    @Mapping(target = "formaPago.idFormaPago", source = "idFormaPago")
    @Mapping(target = "usuario.idUsuario", source = "idUsuario")
    @Mapping(target = "detalles", source = "detalles")
    Cotizacion toEntity(CotizacionCreateDTO dto);


    // DETALLE (RequestDTO -> Entity)

    @Mapping(target = "idDetalle", ignore = true)
    @Mapping(target = "cotizacion", ignore = true)

    // Campos calculados por línea (Ignorar, el service los calcula)
    @Mapping(target = "subTotal", ignore = true) // Corregido: 'subTotal' (CamelCase)
    @Mapping(target = "totalLinea", ignore = true)
    @Mapping(target = "costoTotal", ignore = true)
    @Mapping(target = "utilidadUnitaria", ignore = true)
    @Mapping(target = "precioVentaUnit", ignore = true)
    @Mapping(target = "utilidadBruta", ignore = true)
    @Mapping(target = "inversionTotalIncIgv", ignore = true)
    @Mapping(target = "impuestoMonto", ignore = true)

    // Mapeos
    @Mapping(target = "producto.idProducto", source = "idProducto")
        // Si tu DTO de creación tiene idProveedor, descomenta la siguiente línea:
        // @Mapping(target = "proveedor.idProveedor", source = "idProveedor")
    CotizacionDetalle toDetalleEntity(CotizacionDetalleCreateDTO dto);

    @AfterMapping
    default void linkDetalles(@MappingTarget Cotizacion cotizacion) {
        if (cotizacion.getDetalles() != null) {
            cotizacion.getDetalles().forEach(detalle -> detalle.setCotizacion(cotizacion));
        }
    }
}