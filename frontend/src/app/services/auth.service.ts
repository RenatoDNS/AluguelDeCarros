import { HttpClient } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { map, of, tap } from 'rxjs';

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
  userType: UserType;
};

export type UserType = 'client' | 'agent';

const TOKEN_STORAGE_KEY = 'auth_token';
const USER_TYPE_STORAGE_KEY = 'auth_user_type';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly tokenState = signal<string | null>(sessionStorage.getItem(TOKEN_STORAGE_KEY));
  private readonly userTypeState = signal<UserType | null>(
    sessionStorage.getItem(USER_TYPE_STORAGE_KEY) as UserType | null,
  );

  readonly token = computed(() => this.tokenState());
  readonly userType = computed(() => this.userTypeState());
  readonly isAuthenticated = computed(() => Boolean(this.tokenState()));
  readonly isClient = computed(() => this.userTypeState() === 'client');
  readonly isAgent = computed(() => this.userTypeState() === 'agent');

  login(payload: LoginPayload) {
    const body: LoginRequest = {
      login: payload.cpf,
      senha: payload.password,
    };

    // return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, body).pipe(
    //   tap((response) => {
    //     sessionStorage.setItem(TOKEN_STORAGE_KEY, response.token);
    //     sessionStorage.setItem(USER_TYPE_STORAGE_KEY, response.userType);
    //     this.tokenState.set(response.token);
    //     this.userTypeState.set(response.userType);
    //   }),
    //   map(() => void 0),
    // );

    const response: LoginResponse = {
      token: 'mock-token-client',
      expiresIn: 3600,
      userType: 'client',
    };

    void body;

    return of(response).pipe(
      tap((mockResponse) => {
        sessionStorage.setItem(TOKEN_STORAGE_KEY, mockResponse.token);
        sessionStorage.setItem(USER_TYPE_STORAGE_KEY, mockResponse.userType);
        this.tokenState.set(mockResponse.token);
        this.userTypeState.set(mockResponse.userType);
      }),
      map(() => void 0),
    );
  }

  hasUserType(userType: UserType) {
    return this.userTypeState() === userType;
  }

  getDashboardRoute() {
    return this.isClient() ? '/dashboard/descobrir' : '/dashboard';
  }

  logout() {
    sessionStorage.removeItem(TOKEN_STORAGE_KEY);
    sessionStorage.removeItem(USER_TYPE_STORAGE_KEY);
    this.tokenState.set(null);
    this.userTypeState.set(null);
  }
}
