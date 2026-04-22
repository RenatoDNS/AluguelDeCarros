import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

import { forkJoin, map } from 'rxjs';

import { environment } from '../../environments/environment';
import {
  type PedidoAvaliacaoRequest,
  type PedidoCreditoRequest,
  type PedidoRequest,
  type PedidoResponse,
} from '../models/pedido';

@Injectable({ providedIn: 'root' })
export class PedidoService {
  private readonly http = inject(HttpClient);

  create(payload: PedidoRequest) {
    return this.http.post(`${environment.apiUrl}/pedidos/aluguel`, payload);
  }

  createBancoPedido(payload: PedidoCreditoRequest) {
    return this.http.post<PedidoResponse>(`${environment.apiUrl}/pedidos/credito`, payload);
  }

  listMine() {
    return this.http.get<PedidoResponse[]>(`${environment.apiUrl}/pedidos/me`);
  }

  listAgentPending() {
    return this.http.get<PedidoResponse[]>(`${environment.apiUrl}/agente/pedidos/EM_ANALISE`);
  }

  listAgentFinished() {
    return forkJoin([
      this.http.get<PedidoResponse[]>(`${environment.apiUrl}/agente/pedidos/APROVADO`),
      this.http.get<PedidoResponse[]>(`${environment.apiUrl}/agente/pedidos/REJEITADO`),
    ]).pipe(map(([aprovados, rejeitados]) => [...aprovados, ...rejeitados]));
  }

  cancel(id: number) {
    return this.http.post<PedidoResponse>(`${environment.apiUrl}/pedidos/${id}/cancelar`, {});
  }

  evaluate(id: number, payload: PedidoAvaliacaoRequest) {
    return this.http.post(`${environment.apiUrl}/agente/pedidos/${id}/avaliar`, payload);
  }
}
