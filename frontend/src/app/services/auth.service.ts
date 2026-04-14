import { HttpClient } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { map, of, tap } from 'rxjs';

import { environment } from '../../environments/environment';

type LoginPayload = {
  login: string;
  password: string;
};

type LoginRequest = {
  login: string;
  senha: string;
};

export type UserType = 'cliente' | 'empresa' | 'banco';

export type EntidadeEmpregadoraPayload = {
  nomeEmpresa: string;
  cnpj: string;
  rendimento: number;
};

export type ClienteRegisterPayload = {
  userType: 'cliente';
  rg: string;
  cpf: string;
  nome: string;
  endereco: string;
  profissao: string;
  password: string;
  entidadesEmpregadoras: EntidadeEmpregadoraPayload[];
};

export type EmpresaRegisterPayload = {
  userType: 'empresa';
  razaoSocial: string;
  cnpj: string;
  ramoDeAtividade: string;
  password: string;
};

export type BancoRegisterPayload = {
  userType: 'banco';
  razaoSocial: string;
  cnpj: string;
  codigoBancario: string;
  password: string;
};

export type RegisterPayload =
  | ClienteRegisterPayload
  | EmpresaRegisterPayload
  | BancoRegisterPayload;

type LoginResponse = {
  token: string;
  expiresIn: number;
  userType: UserType;
};

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
  readonly isCliente = computed(() => this.userTypeState() === 'cliente');

  login(payload: LoginPayload) {
    const body: LoginRequest = {
      login: payload.login,
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
      token: 'mock-token-cliente',
      expiresIn: 3600,
      userType: 'cliente',
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

  register(payload: RegisterPayload) {
    // return this.http.post(`${environment.apiUrl}/auth/register`, payload).pipe(map(() => void 0));

    void payload;

    return of(null).pipe(map(() => void 0));
  }

  hasUserType(userType: UserType) {
    return this.userTypeState() === userType;
  }

  logout() {
    sessionStorage.removeItem(TOKEN_STORAGE_KEY);
    sessionStorage.removeItem(USER_TYPE_STORAGE_KEY);
    this.tokenState.set(null);
    this.userTypeState.set(null);
  }
}
