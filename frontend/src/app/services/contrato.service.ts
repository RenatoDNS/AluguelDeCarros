import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { type ContratoCreditoResponse, type ContratoResponse } from '../models/contrato';
import { type PedidoTipo } from '../models/pedido';

type ContratoLookupResponse = ContratoResponse | ContratoCreditoResponse;
type ContratoSignResponse = ContratoResponse | ContratoCreditoResponse;

@Injectable({ providedIn: 'root' })
export class ContratoService {
  private readonly http = inject(HttpClient);

  getByPedido(pedidoId: number, tipoPedido: PedidoTipo): Observable<ContratoLookupResponse> {
    if (tipoPedido === 'ALUGUEL') {
      return this.http.get<ContratoResponse>(`${environment.apiUrl}/contratos/${pedidoId}`);
    }

    return this.http.get<ContratoCreditoResponse>(
      `${environment.apiUrl}/contratos-credito/${pedidoId}`,
    );
  }

  sign(contratoId: number, tipoPedido: PedidoTipo): Observable<ContratoSignResponse> {
    if (tipoPedido === 'ALUGUEL') {
      return this.http.post<ContratoResponse>(`${environment.apiUrl}/contratos/${contratoId}/assinar`, {});
    }

    return this.http.post<ContratoCreditoResponse>(
      `${environment.apiUrl}/contratos-credito/${contratoId}/assinar`,
      {},
    );
  }
}
