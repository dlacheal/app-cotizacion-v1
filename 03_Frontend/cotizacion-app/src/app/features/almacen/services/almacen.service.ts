import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Almacen } from '../models/almacen.model';
import { getApiBaseUrl } from '../../../shared/utils/api.config';

@Injectable({
  providedIn: 'root',
})
export class AlmacenService {

  private readonly API_URL = `${getApiBaseUrl()}/almacenes`;

  constructor(private http: HttpClient) { }

  getAlmacenes(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.API_URL}?page=${page}&size=${size}`);
  }

  getAlmacenesTodos(): Observable<Almacen[]> {
    return this.http.get<Almacen[]>(`${this.API_URL}/lista`);
  }

  getAlmacenById(id: number): Observable<Almacen> {
    return this.http.get<Almacen>(`${this.API_URL}/${id}`);
  }

  crearAlmacen(almacen: Omit<Almacen, 'idAlmacen'>): Observable<Almacen> {
    return this.http.post<Almacen>(this.API_URL, almacen);
  }

  actualizarAlmacen(id: number, almacen: Omit<Almacen, 'idAlmacen'>): Observable<Almacen> {
    return this.http.put<Almacen>(`${this.API_URL}/${id}`, almacen);
  }

  eliminarAlmacen(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
