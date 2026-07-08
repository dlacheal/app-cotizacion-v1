/**
 * Representa el detalle de una línea de cotización.
 * Mapea: CotizacionDetalleResponseDTO.java
 */
export interface CotizacionDetalle {
    idDetalle: number;
    idProducto: number;
    productoNombre: string;
    codigoArticulo: string;
    unidadMedida: string;
    descripcionItem: string;

    numeroLinea: number;
    cantidad: number;

    // Datos financieros por línea
    costoUnitario: number;
    porcentajeGanancia: number;
    precioVentaUnit: number;    // El precio calculado
    subTotal: number;           // Cantidad * PrecioVenta
    impuestoMonto: number;      // IGV del item
    totalLinea: number;         // SubTotal + IGV


} 