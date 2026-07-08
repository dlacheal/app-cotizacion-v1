import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Proveedor } from '../models/proveedor.model';
import { getApiBaseUrl } from '../../../shared/utils/api.config';

@Injectable({
  providedIn: 'root',
})
export class ProveedorService {

  private readonly API_URL = `${getApiBaseUrl()}/proveedores`;

  constructor(private http: HttpClient) { }

  getProveedores(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.API_URL}?page=${page}&size=${size}`);
  }

  getProveedoresTodos(): Observable<Proveedor[]> {
    return this.http.get<any>(`${this.API_URL}?page=0&size=1000`).pipe(
      map(res => res.content || [])
    );
  }

  getProveedorById(id: number): Observable<Proveedor> {
    return this.http.get<Proveedor>(`${this.API_URL}/${id}`);
  }

  crearProveedor(proveedor: Omit<Proveedor, 'idProveedor'>): Observable<Proveedor> {
    return this.http.post<Proveedor>(this.API_URL, proveedor);
  }

  actualizarProveedor(id: number, proveedor: Omit<Proveedor, 'idProveedor'>): Observable<Proveedor> {
    return this.http.put<Proveedor>(`${this.API_URL}/${id}`, proveedor);
  }

  eliminarProveedor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
