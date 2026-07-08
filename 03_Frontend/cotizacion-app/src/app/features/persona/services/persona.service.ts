import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Persona } from '../models/persona.model';
import { getApiBaseUrl } from '../../../shared/utils/api.config';

@Injectable({
  providedIn: 'root',
})
export class PersonaService {

  private readonly API_URL = `${getApiBaseUrl()}/personas`;

  constructor(private http: HttpClient) { }

  getPersonas(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.API_URL}?page=${page}&size=${size}`);
  }

  getPersonasTodas(): Observable<Persona[]> {
    return this.http.get<Persona[]>(`${this.API_URL}/lista`);
  }

  getPersonaById(id: number): Observable<Persona> {
    return this.http.get<Persona>(`${this.API_URL}/${id}`);
  }

  crearPersona(persona: Omit<Persona, 'idPersona'>): Observable<Persona> {
    return this.http.post<Persona>(this.API_URL, persona);
  }

  existsByDni(dni: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.API_URL}/validar-existencia/${dni}`);
  }

  actualizarPersona(id: number, persona: Omit<Persona, 'idPersona'>): Observable<Persona> {
    return this.http.put<Persona>(`${this.API_URL}/${id}`, persona);
  }

  eliminarPersona(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
