import { CotizacionDetalle } from "./cotizacionDetalle.model";


/**
 * Representa la cabecera y el cuerpo completo de la respuesta de cotización.
 * Mapea: CotizacionResponseDTO.java
 */
export interface Cotizacion {
    // View
    idCotizacion: number;
    // Formato recibido: "yyyy-MM-dd HH:mm:ss"
    fecha: string;
    usuarioUsername: string;

    // Datos aplanados (Flattened)
    clienteNombre: string;
    clienteRucDni: string;
    clienteTelefono: string;
    clienteEmail: string;
    clienteDireccion: string;

    // View1
    monedaCodigo: string;
    monedaDescripcion: string;
    formaPagoNombre: string;
    estadoNombre: string;

    validezDias: number;
    observaciones: string;

    // View2
    // --- NUEVOS CAMPOS CALCULADOS ---
    inversionTotal: number;      // Costo acumulado
    utilidadBrutaTotal: number;  // Ganancia neta
    subTotal: number;            // Venta sin IGV
    montoIgv: number;            // Impuesto calculado
    totalGeneral: number;        // Venta Total (SubTotal + IGV)

    detalles: CotizacionDetalle[];
}