import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { getApiBaseUrl } from '../../shared/utils/api.config';

export interface LoginResponse {
  token: string;
  username: string;
  nombrePersona: string;
  roles: string[];
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = `${getApiBaseUrl()}/auth/login`;

  // Signals
  private readonly _token = signal<string | null>(localStorage.getItem('jwt_token'));
  private readonly _username = signal<string | null>(localStorage.getItem('username'));
  private readonly _nombrePersona = signal<string | null>(localStorage.getItem('nombre_persona'));
  private readonly _roles = signal<string[]>(JSON.parse(localStorage.getItem('roles') || '[]'));

  public readonly token = this._token.asReadonly();
  public readonly username = this._username.asReadonly();
  public readonly nombrePersona = this._nombrePersona.asReadonly();
  public readonly roles = this._roles.asReadonly();

  public readonly isLoggedIn = computed(() => !!this._token());

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.API_URL, { username, password }).pipe(
      tap(response => {
        localStorage.setItem('jwt_token', response.token);
        localStorage.setItem('username', response.username);
        localStorage.setItem('nombre_persona', response.nombrePersona);
        localStorage.setItem('roles', JSON.stringify(response.roles));

        this._token.set(response.token);
        this._username.set(response.username);
        this._nombrePersona.set(response.nombrePersona);
        this._roles.set(response.roles);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('username');
    localStorage.removeItem('nombre_persona');
    localStorage.removeItem('roles');

    this._token.set(null);
    this._username.set(null);
    this._nombrePersona.set(null);
    this._roles.set([]);
  }

  hasRole(roleName: string): boolean {
    return this._roles().includes(roleName);
  }
}
