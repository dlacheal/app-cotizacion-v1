import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MetodoPago } from '../models/metodo-pago.model';
import { getApiBaseUrl } from '../../../shared/utils/api.config';

@Injectable({
  providedIn: 'root',
})
export class MetodoPagoService {

  private readonly API_URL = `${getApiBaseUrl()}/formas-pago`;

  constructor(private http: HttpClient) { }

  getMetodosPago(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.API_URL}?page=${page}&size=${size}`);
  }

  getMetodosPagoTodos(): Observable<MetodoPago[]> {
    return this.http.get<MetodoPago[]>(`${this.API_URL}/lista`);
  }

  getMetodoPagoById(id: number): Observable<MetodoPago> {
    return this.http.get<MetodoPago>(`${this.API_URL}/${id}`);
  }

  crearMetodoPago(metodo: Omit<MetodoPago, 'idFormaPago'>): Observable<MetodoPago> {
    return this.http.post<MetodoPago>(this.API_URL, metodo);
  }

  actualizarMetodoPago(id: number, metodo: Omit<MetodoPago, 'idFormaPago'>): Observable<MetodoPago> {
    return this.http.put<MetodoPago>(`${this.API_URL}/${id}`, metodo);
  }

  eliminarMetodoPago(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
