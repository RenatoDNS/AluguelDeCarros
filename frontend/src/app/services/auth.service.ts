import { HttpClient } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { map, tap } from 'rxjs';

import { environment } from '../../environments/environment';

type LoginPayload = {
  cpf: string;
  password: string;
};

type LoginRequest = {
  login: string;
  senha: string;
};

type LoginResponse = {
  token: string;
  expiresIn: number;
};

const TOKEN_STORAGE_KEY = 'auth_token';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly tokenState = signal<string | null>(sessionStorage.getItem(TOKEN_STORAGE_KEY));

  readonly token = computed(() => this.tokenState());
  readonly isAuthenticated = computed(() => Boolean(this.tokenState()));

  login(payload: LoginPayload) {
    const body: LoginRequest = {
      login: payload.cpf,
      senha: payload.password,
    };

    return this.http
      .post<LoginResponse>(`${environment.apiUrl}/auth/login`, body)
      .pipe(
        tap((response) => {
          sessionStorage.setItem(TOKEN_STORAGE_KEY, response.token);
          this.tokenState.set(response.token);
        }),
        map(() => void 0),
      );
  }

  logout() {
    sessionStorage.removeItem(TOKEN_STORAGE_KEY);
    this.tokenState.set(null);
  }
}
