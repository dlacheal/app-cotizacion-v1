import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente.model';
import { getApiBaseUrl } from '../../../shared/utils/api.config';

@Injectable({
  providedIn: 'root',
})
export class ClienteService {

  private readonly API_URL = `${getApiBaseUrl()}/clientes`;

  constructor(private http: HttpClient) { }

  getClientes(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.API_URL}?page=${page}&size=${size}`);
  }

  getClientesTodos(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${this.API_URL}/lista`);
  }

  getClienteById(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.API_URL}/${id}`);
  }

  crearCliente(cliente: Omit<Cliente, 'idCliente'>): Observable<Cliente> {
    return this.http.post<Cliente>(this.API_URL, cliente);
  }

  actualizarCliente(id: number, cliente: Omit<Cliente, 'idCliente'>): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.API_URL}/${id}`, cliente);
  }

  eliminarCliente(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
