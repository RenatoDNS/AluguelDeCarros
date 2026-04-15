import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map } from 'rxjs';

import { environment } from '../../environments/environment';
import { type Veiculo, type VeiculoCreatePayload, type VeiculoUpdatePayload } from '../models/veiculo';

@Injectable({ providedIn: 'root' })
export class VeiculoService {
  private readonly http = inject(HttpClient);

  list() {
    return this.http.get<Veiculo[]>(`${environment.apiUrl}/automoveis`);
  }

  listMine() {
    return this.http.get<Veiculo[]>(`${environment.apiUrl}/automoveis/me`);
  }

  create(payload: VeiculoCreatePayload) {
    return this.http.post<Veiculo>(`${environment.apiUrl}/automoveis`, payload).pipe(map(() => void 0));
  }

  update(id: number, payload: VeiculoUpdatePayload) {
    return this.http.put<Veiculo>(`${environment.apiUrl}/automoveis/${id}`, payload).pipe(map(() => void 0));
  }
}
