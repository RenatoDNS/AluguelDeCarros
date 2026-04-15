import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

import { environment } from '../../environments/environment';
import {
  type PedidoAvaliacaoRequest,
  type PedidoRequest,
  type PedidoResponse,
} from '../models/pedido';

@Injectable({ providedIn: 'root' })
export class PedidoService {
  private readonly http = inject(HttpClient);

  create(payload: PedidoRequest) {
    return this.http.post(`${environment.apiUrl}/pedidos`, payload);
  }

  listMine() {
    return this.http.get<PedidoResponse[]>(`${environment.apiUrl}/pedidos/me`);
  }

  listAgentPending() {
    return this.http.get<PedidoResponse[]>(`${environment.apiUrl}/agente/pedidos/em-analise`);
  }

  evaluate(id: number, payload: PedidoAvaliacaoRequest) {
    return this.http.post(`${environment.apiUrl}/agente/pedidos/${id}/avaliar`, payload);
  }
}
