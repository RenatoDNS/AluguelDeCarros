import { HttpClient } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { BehaviorSubject, catchError, filter, map, switchMap, take, tap, throwError } from 'rxjs';

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

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly tokenState = signal<string | null>(sessionStorage.getItem(TOKEN_STORAGE_KEY));
  private readonly userTypeState = signal<UserType | null>(
    sessionStorage.getItem(USER_TYPE_STORAGE_KEY) as UserType | null,
  );
  private readonly authReadyState = signal(false);
  private readonly authReady$ = new BehaviorSubject(false);

  readonly token = computed(() => this.tokenState());
  readonly userType = computed(() => this.userTypeState());
  readonly isAuthenticated = computed(() => Boolean(this.tokenState()));
  readonly isCliente = computed(() => this.userTypeState() === 'cliente');
  readonly isReady = computed(() => this.authReadyState());

  constructor() {
    this.initializeAuthState();
  }

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

  waitUntilReady() {
    return this.authReady$.pipe(filter(Boolean), take(1));
  }

  logout() {
    this.clearAuthState();
  }

  private initializeAuthState() {
    if (!this.tokenState()) {
      this.setAuthReady();
      return;
    }

    this.me().subscribe({
      next: () => {
        this.setAuthReady();
      },
      error: () => {
        this.clearAuthState();
        this.setAuthReady();
      },
    });
  }

  private setAuthReady() {
    this.authReadyState.set(true);
    this.authReady$.next(true);
  }

  private clearAuthState() {
    sessionStorage.removeItem(TOKEN_STORAGE_KEY);
    sessionStorage.removeItem(USER_TYPE_STORAGE_KEY);
    this.tokenState.set(null);
    this.userTypeState.set(null);
  }
}
