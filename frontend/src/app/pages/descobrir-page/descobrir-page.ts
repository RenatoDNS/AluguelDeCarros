import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';

import { VeiculoCardComponent } from '../../components/veiculo-card/veiculo-card';
import { Veiculo } from '../../models/veiculo';
import { VeiculoService } from '../../services/veiculo.service';

@Component({
  selector: 'app-descobrir-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [VeiculoCardComponent],
  templateUrl: './descobrir-page.html',
  styleUrl: './descobrir-page.css',
})
export class DescobrirPageComponent {
  private readonly veiculoService = inject(VeiculoService);

  readonly veiculos = signal<Veiculo[]>([]);

  constructor() {
    this.veiculoService.list().subscribe((veiculos) => this.veiculos.set(veiculos));
  }
}
