/*import { Injectable } from '@angular/core';
import { Cotizacion } from '../models/cotizacion.model';
import { cotizacionData } from '../data/cotizacion.data';
import { CotizacionDetalle } from '../models/cotizacionDetalle.model';

@Injectable({
  providedIn: 'root',
})
export class CotizacionService {

  public cotizacion: Cotizacion = cotizacionData;

  constructor() { }

  getCotizacion(): Cotizacion {
    this.cotizacion = this.calculateTotal();
    return this.cotizacion
  }

  removeDetalle(idDetalle: number) {
    this.cotizacion.detalles = this.cotizacion.detalles.filter(detalle => detalle.idDetalle !== idDetalle);
    this.cotizacion = this.calculateTotal();
  }

  calculateTotal(): Cotizacion {
    let subTotal = 0;
    let igv = 0;
    let inversionTotal = 0;
    let utilidadBrutaTotal = 0;

    this.cotizacion.detalles.forEach(detalle => {
      subTotal += detalle.cantidad * detalle.precioVentaUnit;
      igv += detalle.cantidad * detalle.precioVentaUnit * 0.18;
      inversionTotal += detalle.cantidad * detalle.costoUnitario;
      utilidadBrutaTotal += detalle.cantidad * detalle.precioVentaUnit - detalle.cantidad * detalle.costoUnitario;
    });

    this.cotizacion.totalGeneral = subTotal + igv;
    this.cotizacion.subTotal = subTotal;
    this.cotizacion.montoIgv = igv;
    this.cotizacion.inversionTotal = inversionTotal;
    this.cotizacion.utilidadBrutaTotal = utilidadBrutaTotal;
    return this.cotizacion;
  }

  calculateTotalItem(item: CotizacionDetalle): CotizacionDetalle {
    item.precioVentaUnit = Number(item.costoUnitario) * (1 + Number(item.porcentajeGanancia) / 100);
    item.subTotal = Number(item.cantidad) * item.precioVentaUnit;
    item.impuestoMonto = item.subTotal * 0.18;
    item.totalLinea = item.subTotal + item.impuestoMonto;
    return item;
  }

  addItem(item: CotizacionDetalle): Cotizacion {

    this.calculateTotalItem(item);

    this.cotizacion.detalles = [...this.cotizacion.detalles, item];
    this.cotizacion = this.calculateTotal();
    return { ...this.cotizacion };
  }


} 
*/

import { Injectable } from '@angular/core';
import { Cotizacion } from '../models/cotizacion.model';
import { cotizacionData } from '../data/cotizacion.data';
import { CotizacionDetalle } from '../models/cotizacionDetalle.model';

import { HttpClient } from '@angular/common/http';
import { getApiBaseUrl } from '../../../shared/utils/api.config';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CotizacionService {

  private readonly API_URL = `${getApiBaseUrl()}/cotizaciones`;
  private readonly IGV_RATE = 0.18;

  public cotizacion: Cotizacion = cotizacionData;

  constructor(private http: HttpClient) { }

  listarTodas(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.API_URL}?page=${page}&size=${size}`);
  }

  listarTodasTodos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/lista`);
  }

  obtenerCotizacion(id: number): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/${id}`);
  }

  crearCotizacion(createDTO: any): Observable<any> {
    return this.http.post<any>(this.API_URL, createDTO);
  }

  actualizarCotizacion(id: number, updateDTO: any): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/${id}`, updateDTO);
  }

  eliminarCotizacion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  // Redondeo bancario básico
  private round(value: number, decimals = 2): number {
    const p = Math.pow(10, decimals);
    return Math.round((value + Number.EPSILON) * p) / p;
  }

  getCotizacion(): Cotizacion {
    this.cotizacion = this.calculateTotal();
    return this.cotizacion;
  }

  /*
  removeDetalle(idDetalle: number) {

    console.log(idDetalle);
    console.log(this.cotizacion.detalles);
    this.cotizacion.detalles = this.cotizacion.detalles.filter(d => d.idDetalle !== idDetalle);
    this.cotizacion = this.calculateTotal();
  }
  */
  removeDetalle(idDetalle: number): Cotizacion {
    this.cotizacion.detalles = this.cotizacion.detalles.filter(d => d.idDetalle !== idDetalle);
    this.cotizacion = this.calculateTotal();

    // MUY IMPORTANTE: devolver nueva referencia
    return { ...this.cotizacion, detalles: [...this.cotizacion.detalles] };
  }


  calculateTotal(): Cotizacion {
    // Asegura que cada item esté recalculado/redondeado antes de sumar
    const detalles = this.cotizacion.detalles.map(d => this.calculateTotalItem({ ...d }));

    let subTotal = 0;
    let igv = 0;
    let inversionTotal = 0;
    let utilidadBrutaTotal = 0;

    for (const d of detalles) {
      subTotal += d.subTotal;               // ya redondeado
      igv += d.impuestoMonto;               // ya redondeado
      inversionTotal += this.round(d.cantidad * d.costoUnitario, 2);
      utilidadBrutaTotal += this.round(d.subTotal - (d.cantidad * d.costoUnitario), 2);
    }

    subTotal = this.round(subTotal, 2);
    igv = this.round(igv, 2);

    this.cotizacion.detalles = detalles;
    this.cotizacion.subTotal = subTotal;
    this.cotizacion.montoIgv = igv;
    this.cotizacion.totalGeneral = this.round(subTotal + igv, 2);
    this.cotizacion.inversionTotal = this.round(inversionTotal, 2);
    this.cotizacion.utilidadBrutaTotal = this.round(utilidadBrutaTotal, 2);

    return this.cotizacion;
  }

  calculateTotalItem(item: CotizacionDetalle): CotizacionDetalle {
    const costo = Number(item.costoUnitario);
    const gan = Number(item.porcentajeGanancia);
    const cant = Number(item.cantidad);

    // Unitario con 4 decimales (como tu backend: 4900.0000)
    const precioVentaUnit = this.round(costo * (1 + gan / 100), 4);

    // Montos monetarios con 2 decimales
    const subTotal = this.round(cant * precioVentaUnit, 2);
    const impuestoMonto = this.round(subTotal * this.IGV_RATE, 2);
    const totalLinea = this.round(subTotal + impuestoMonto, 2);

    return {
      ...item,
      precioVentaUnit,
      subTotal,
      impuestoMonto,
      totalLinea
    };
  }

  addItem(item: CotizacionDetalle): Cotizacion {
    const itemCalc = this.calculateTotalItem({ ...item });
    this.cotizacion.detalles = [...this.cotizacion.detalles, itemCalc];
    this.cotizacion = this.calculateTotal();
    return { ...this.cotizacion };
  }

  getEstados(): Observable<any[]> {
    return this.http.get<any[]>(`${getApiBaseUrl()}/estados-cotizacion`);
  }
}
