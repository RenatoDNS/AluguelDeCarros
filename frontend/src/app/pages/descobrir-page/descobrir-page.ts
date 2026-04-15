import { CurrencyPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { finalize } from 'rxjs';

import { VeiculoCardComponent } from '../../components/veiculo-card/veiculo-card';
import { Veiculo } from '../../models/veiculo';
import { AuthService } from '../../services/auth.service';
import { PedidoService } from '../../services/pedido.service';
import { VeiculoService } from '../../services/veiculo.service';

type DialogStep = 'inicio' | 'fim';

@Component({
  selector: 'app-descobrir-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, VeiculoCardComponent, CurrencyPipe],
  templateUrl: './descobrir-page.html',
  styleUrl: './descobrir-page.css',
})
export class DescobrirPageComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly pedidoService = inject(PedidoService);
  private readonly veiculoService = inject(VeiculoService);

  readonly veiculos = signal<Veiculo[]>([]);
  readonly selectedVeiculo = signal<Veiculo | null>(null);
  readonly dialogOpen = signal(false);
  readonly dialogStep = signal<DialogStep>('inicio');
  readonly loading = signal(false);
  readonly errorMessage = signal('');
  readonly successMessage = signal('');
  readonly minDataFim = signal('');
  readonly isInicioStep = computed(() => this.dialogStep() === 'inicio');
  readonly aluguelForm = this.formBuilder.nonNullable.group({
    dataInicio: ['', [Validators.required]],
    dataFim: ['', [Validators.required]],
  });

  totalPreview() {
    const veiculo = this.selectedVeiculo();
    const { dataInicio, dataFim } = this.aluguelForm.getRawValue();

    if (!veiculo || !dataInicio || !dataFim) {
      return null;
    }

    const startDate = new Date(dataInicio);
    const endDate = new Date(dataFim);

    if (endDate <= startDate) {
      return null;
    }

    const millisecondsPerDay = 1000 * 60 * 60 * 24;
    const diffInMilliseconds = endDate.getTime() - startDate.getTime();
    const days = Math.ceil(diffInMilliseconds / millisecondsPerDay);

    return days * veiculo.diaria;
  }

  constructor() {
    this.veiculoService.list().subscribe((veiculos) => this.veiculos.set(veiculos));
  }

  openAluguelDialog(veiculo: Veiculo) {
    this.resetDialogState();
    this.selectedVeiculo.set(veiculo);
    this.dialogOpen.set(true);
  }

  closeAluguelDialog() {
    this.dialogOpen.set(false);
    this.resetDialogState();
  }

  nextStep() {
    const dataInicio = this.aluguelForm.controls.dataInicio.value;

    if (!dataInicio) {
      this.aluguelForm.controls.dataInicio.markAsTouched();
      return;
    }

    this.errorMessage.set('');
    this.minDataFim.set(dataInicio);
    this.dialogStep.set('fim');
  }

  submitPedido() {
    const profile = this.authService.profile();
    const veiculo = this.selectedVeiculo();
    const { dataInicio, dataFim } = this.aluguelForm.getRawValue();

    if (!profile?.id || !veiculo) {
      this.errorMessage.set('Não foi possível identificar o cliente ou o veículo selecionado.');
      return;
    }

    if (!dataInicio) {
      this.aluguelForm.controls.dataInicio.markAsTouched();
      this.dialogStep.set('inicio');
      return;
    }

    if (!dataFim) {
      this.aluguelForm.controls.dataFim.markAsTouched();
      return;
    }

    if (new Date(dataFim) <= new Date(dataInicio)) {
      this.errorMessage.set('A data de fim deve ser maior que a data de início.');
      this.aluguelForm.controls.dataFim.markAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.pedidoService
      .create({
        clienteId: profile.id,
        automovelId: veiculo.id,
        dataInicio,
        dataFim,
        status: 'EM_ANALISE',
      })
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => {
          this.successMessage.set('Pedido realizado com sucesso.');
          setTimeout(() => this.closeAluguelDialog(), 1200);
        },
        error: (error) => {
          this.errorMessage.set(
            error?.error?.mensagem ?? 'Não foi possível realizar o pedido. Tente novamente.',
          );
        },
      });
  }

  private resetDialogState() {
    this.selectedVeiculo.set(null);
    this.dialogStep.set('inicio');
    this.loading.set(false);
    this.errorMessage.set('');
    this.successMessage.set('');
    this.minDataFim.set('');
    this.aluguelForm.reset({
      dataInicio: '',
      dataFim: '',
    });
  }
}
