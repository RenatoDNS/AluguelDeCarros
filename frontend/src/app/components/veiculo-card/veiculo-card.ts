import { ChangeDetectionStrategy, Component, input, output } from '@angular/core';

import { Veiculo } from '../../models/veiculo';

@Component({
  selector: 'app-veiculo-card',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './veiculo-card.html',
  styleUrl: './veiculo-card.css',
})
export class VeiculoCardComponent {
  readonly veiculo = input.required<Veiculo>();
  readonly alugar = output<Veiculo>();

  requestAluguel() {
    this.alugar.emit(this.veiculo());
  }
}
