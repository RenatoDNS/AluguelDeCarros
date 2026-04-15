import { inject, Injectable } from '@angular/core';

import { Veiculo } from '../models/veiculo';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class VeiculoService {
  private readonly http = inject(HttpClient);

  list() {
    return this.http.get<Veiculo[]>(`${environment.apiUrl}/automoveis`);
  }
}
