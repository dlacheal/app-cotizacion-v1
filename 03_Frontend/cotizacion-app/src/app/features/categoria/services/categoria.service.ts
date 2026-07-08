import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Categoria } from '../models/categoria.model';
import { getApiBaseUrl } from '../../../shared/utils/api.config';

@Injectable({
  providedIn: 'root',
})
export class CategoriaService {

  private readonly API_URL = `${getApiBaseUrl()}/categorias`;

  constructor(private http: HttpClient) { }

  getCategorias(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.API_URL}?page=${page}&size=${size}`);
  }

  getCategoriaById(id: number): Observable<Categoria> {
    return this.http.get<Categoria>(`${this.API_URL}/${id}`);
  }

  crearCategoria(categoria: { nombre: string; descripcion?: string }): Observable<Categoria> {
    return this.http.post<Categoria>(this.API_URL, categoria);
  }

  actualizarCategoria(id: number, categoria: { nombre: string; descripcion?: string }): Observable<Categoria> {
    return this.http.put<Categoria>(`${this.API_URL}/${id}`, categoria);
  }

  eliminarCategoria(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
