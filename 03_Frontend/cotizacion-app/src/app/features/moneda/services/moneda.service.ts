import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Moneda } from '../models/moneda.model';
import { getApiBaseUrl } from '../../../shared/utils/api.config';

@Injectable({
  providedIn: 'root',
})
export class MonedaService {

  private readonly API_URL = `${getApiBaseUrl()}/monedas`;

  constructor(private http: HttpClient) { }

  getMonedas(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.API_URL}?page=${page}&size=${size}`);
  }

  getMonedasTodas(): Observable<Moneda[]> {
    return this.http.get<Moneda[]>(`${this.API_URL}/lista`);
  }

  getMonedaById(id: number): Observable<Moneda> {
    return this.http.get<Moneda>(`${this.API_URL}/id/${id}`);
  }

  crearMoneda(moneda: Omit<Moneda, 'idMoneda'>): Observable<Moneda> {
    return this.http.post<Moneda>(this.API_URL, moneda);
  }

  actualizarMoneda(id: number, moneda: Omit<Moneda, 'idMoneda'>): Observable<Moneda> {
    return this.http.put<Moneda>(`${this.API_URL}/${id}`, moneda);
  }

  eliminarMoneda(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
