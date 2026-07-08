import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { getApiBaseUrl } from '../../../shared/utils/api.config';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {

  private readonly API_URL = `${getApiBaseUrl()}/auth`;

  constructor(private http: HttpClient) { }

  getUsuarios(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/usuarios?page=${page}&size=${size}`);
  }

  getUsuarioById(id: number): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/usuarios/${id}`);
  }

  registrarUsuario(usuario: any): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/register`, usuario);
  }

  actualizarUsuario(id: number, usuario: any): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/usuarios/${id}`, usuario);
  }

  eliminarUsuario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/usuarios/${id}`);
  }
}
