import { Injectable } from '@angular/core';
import { of } from 'rxjs';

import { Veiculo } from '../models/veiculo';

const VEICULOS_MOCK: Veiculo[] = [
  {
    matricula: 'VEI-2026-001',
    placa: 'ABC1D23',
    marca: 'Toyota',
    modelo: 'Corolla',
    ano: 2023,
  },
  {
    matricula: 'VEI-2026-002',
    placa: 'EFG4H56',
    marca: 'Honda',
    modelo: 'Civic',
    ano: 2022,
  },
  {
    matricula: 'VEI-2026-003',
    placa: 'IJK7L89',
    marca: 'Jeep',
    modelo: 'Compass',
    ano: 2024,
  },
  {
    matricula: 'VEI-2026-004',
    placa: 'MNO1P23',
    marca: 'Chevrolet',
    modelo: 'Tracker',
    ano: 2021,
  },
  {
    matricula: 'VEI-2026-005',
    placa: 'QRS4T56',
    marca: 'Volkswagen',
    modelo: 'T-Cross',
    ano: 2023,
  },
  {
    matricula: 'VEI-2026-006',
    placa: 'UVW7X89',
    marca: 'Fiat',
    modelo: 'Pulse',
    ano: 2022,
  },
];

@Injectable({ providedIn: 'root' })
export class VeiculoService {
  list() {
    return of(VEICULOS_MOCK);
  }
}
