import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { getApiBaseUrl } from '../../../shared/utils/api.config';

@Injectable({
  providedIn: 'root',
})
export class AuthorityService {

  private readonly API_URL = `${getApiBaseUrl()}/authorities`;

  constructor(private http: HttpClient) { }

  getRolesByUsuario(idUsuario: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/usuario/${idUsuario}`);
  }
}
