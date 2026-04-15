import { HttpClient } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { catchError, map, switchMap, tap, throwError } from 'rxjs';

import { environment } from '../../environments/environment';
import {
  type AuthMeResponse,
  type LoginPayload,
  type LoginRequest,
  type LoginResponse,
  type RegisterPayload,
  type UserType,
} from '../models/auth';

const TOKEN_STORAGE_KEY = 'auth_token';
const USER_TYPE_STORAGE_KEY = 'auth_user_type';
const PROFILE_STORAGE_KEY = 'auth_me';

function getStoredProfile() {
  const storedProfile = sessionStorage.getItem(PROFILE_STORAGE_KEY);

  if (!storedProfile) {
    return null;
  }

  try {
    return JSON.parse(storedProfile) as AuthMeResponse;
  } catch {
    sessionStorage.removeItem(PROFILE_STORAGE_KEY);
    return null;
  }
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly tokenState = signal<string | null>(sessionStorage.getItem(TOKEN_STORAGE_KEY));
  private readonly userTypeState = signal<UserType | null>(
    sessionStorage.getItem(USER_TYPE_STORAGE_KEY) as UserType | null,
  );
  private readonly profileState = signal<AuthMeResponse | null>(getStoredProfile());

  readonly token = computed(() => this.tokenState());
  readonly userType = computed(() => this.userTypeState());
  readonly profile = computed(() => this.profileState());
  readonly isAuthenticated = computed(() => Boolean(this.tokenState()));
  readonly isCliente = computed(() => this.userTypeState() === 'cliente');

  login(payload: LoginPayload) {
    const body: LoginRequest = {
      login: payload.login,
      senha: payload.password,
    };

    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, body).pipe(
      tap((response) => {
        sessionStorage.setItem(TOKEN_STORAGE_KEY, response.token);
        sessionStorage.setItem(USER_TYPE_STORAGE_KEY, response.userType);
        this.tokenState.set(response.token);
        this.userTypeState.set(response.userType);
      }),
      switchMap(() =>
        this.me().pipe(
          tap((profile) => {
            sessionStorage.setItem(PROFILE_STORAGE_KEY, JSON.stringify(profile));
            this.profileState.set(profile);
          }),
          map(() => void 0),
          catchError((error) => {
            this.clearAuthState();
            return throwError(() => error);
          }),
        ),
      ),
    );
  }

  me() {
    return this.http.get<AuthMeResponse>(`${environment.apiUrl}/auth/me`);
  }

  register(payload: RegisterPayload) {
    if ('cpf' in payload) {
      return this.http.post(`${environment.apiUrl}/clientes`, payload);
    }

    if ('codigoBancario' in payload) {
      return this.http.post(`${environment.apiUrl}/bancos`, payload);
    }

    return this.http.post(`${environment.apiUrl}/empresas`, payload);
  }

  hasUserType(userType: UserType) {
    return this.userTypeState() === userType;
  }

  logout() {
    this.clearAuthState();
  }

  private clearAuthState() {
    sessionStorage.removeItem(TOKEN_STORAGE_KEY);
    sessionStorage.removeItem(USER_TYPE_STORAGE_KEY);
    sessionStorage.removeItem(PROFILE_STORAGE_KEY);
    this.tokenState.set(null);
    this.userTypeState.set(null);
    this.profileState.set(null);
  }
}
