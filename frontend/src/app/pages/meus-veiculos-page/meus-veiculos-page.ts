import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { NgxMaskDirective } from 'ngx-mask';
import { finalize } from 'rxjs';

import { type Veiculo } from '../../models/veiculo';
import { AuthService } from '../../services/auth.service';
import { VeiculoService } from '../../services/veiculo.service';

function parsePercentValue(value: string) {
  const normalizedValue = String(value ?? '').replace('%', '').replace(',', '.').trim();
  const parsedValue = Number(normalizedValue);
  return Number.isFinite(parsedValue) ? parsedValue : Number.NaN;
}

function formatPercentValue(value: number) {
  return `${value.toFixed(2).replace('.', ',')}%`;
}

@Component({
  selector: 'app-meus-veiculos-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './meus-veiculos-page.html',
  styleUrl: './meus-veiculos-page.css',
})
export class MeusVeiculosPageComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly veiculoService = inject(VeiculoService);

  readonly veiculos = signal<Veiculo[]>([]);
  readonly dialogOpen = signal(false);
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly deletingVeiculoId = signal<number | null>(null);
  readonly editingVeiculoId = signal<number | null>(null);
  readonly errorMessage = signal('');
  readonly isBanco = computed(() => this.authService.userType() === 'banco');
  readonly valorLabel = computed(() => (this.isBanco() ? 'Preço do Veículo' : 'Diária'));
  readonly valorErrorMessage = computed(() =>
    this.isBanco() ? 'Informe um preço do veículo válido.' : 'Informe uma diária válida.',
  );
  readonly veiculoForm = this.formBuilder.nonNullable.group({
    matricula: ['', [Validators.required]],
    placa: ['', [Validators.required, Validators.maxLength(7)]],
    ano: ['', [Validators.required, Validators.pattern(/^\d+$/)]],
    marca: ['', [Validators.required]],
    modelo: ['', [Validators.required]],
    valor: ['', [Validators.required]],
    taxaJuros: [''],
  });

  constructor() {
    this.loadVeiculos();
  }

  openCreateDialog() {
    this.resetForm();
    this.dialogOpen.set(true);
  }

  openEditDialog(veiculo: Veiculo) {
    this.resetForm();
    this.editingVeiculoId.set(veiculo.id);
    this.veiculoForm.reset({
      matricula: veiculo.matricula,
      placa: veiculo.placa,
      ano: String(veiculo.ano),
      marca: veiculo.marca,
      modelo: veiculo.modelo,
      valor: this.formatCurrencyValue(veiculo.valor),
      taxaJuros: veiculo.taxaJuros === undefined ? '' : formatPercentValue(veiculo.taxaJuros),
    });
    this.dialogOpen.set(true);
  }

  closeDialog() {
    this.dialogOpen.set(false);
    this.resetForm();
  }

  deleteVeiculo(veiculoId: number) {
    this.deletingVeiculoId.set(veiculoId);
    this.errorMessage.set('');

    this.veiculoService
      .delete(veiculoId)
      .pipe(finalize(() => this.deletingVeiculoId.set(null)))
      .subscribe({
        next: () => {
          this.loadVeiculos();
        },
        error: (error) => {
          this.errorMessage.set(error?.error?.mensagem ?? 'Não foi possível remover o veículo.');
        },
      });
  }

  submit() {
    if (this.veiculoForm.invalid) {
      this.veiculoForm.markAllAsTouched();
      return;
    }

    const { matricula, placa, ano, marca, modelo, valor, taxaJuros } = this.veiculoForm.getRawValue();
    const valorValue = this.parseCurrencyValue(valor);

    if (!Number.isFinite(valorValue) || valorValue <= 0) {
      this.errorMessage.set(this.valorErrorMessage());
      return;
    }

    let taxaJurosValue: number | undefined;

    if (this.isBanco()) {
      if (!String(taxaJuros ?? '').trim()) {
        this.errorMessage.set('Informe a taxa de juros para contrato de crédito.');
        this.veiculoForm.controls.taxaJuros.markAsTouched();
        return;
      }

      taxaJurosValue = parsePercentValue(taxaJuros);

      if (!Number.isFinite(taxaJurosValue)) {
        this.errorMessage.set('Informe uma taxa de juros válida.');
        this.veiculoForm.controls.taxaJuros.markAsTouched();
        return;
      }
    }

    this.saving.set(true);
    this.errorMessage.set('');

    const payload = {
      matricula,
      placa,
      ano: Number(ano),
      marca,
      modelo,
      status: 'DISPONIVEL' as const,
      valor: valorValue,
      ...(this.isBanco() ? { taxaJuros: taxaJurosValue } : {}),
    };

    const request$ = this.editingVeiculoId()
      ? this.veiculoService.update(this.editingVeiculoId()!, payload)
      : this.veiculoService.create(payload);

    request$
      .pipe(finalize(() => this.saving.set(false)))
      .subscribe({
        next: () => {
          this.loadVeiculos();
          this.closeDialog();
        },
        error: (error) => {
          this.errorMessage.set(error?.error?.mensagem ?? 'Não foi possível salvar o veículo.');
        },
      });
  }

  private loadVeiculos() {
    this.loading.set(true);
    this.errorMessage.set('');

    this.veiculoService.listMine().subscribe({
      next: (veiculos) => {
        this.veiculos.set(veiculos);
        this.loading.set(false);
      },
      error: (error) => {
        this.errorMessage.set(error?.error?.mensagem ?? 'Não foi possível carregar os veículos.');
        this.loading.set(false);
      },
    });
  }

  private parseCurrencyValue(value: string) {
    return Number(
      String(value).replace(/\s/g, '').replace('R$', '').replace(/\./g, '').replace(',', '.').trim(),
    );
  }

  private formatCurrencyValue(value: number) {
    return `R$ ${value.toFixed(2).replace('.', ',')}`;
  }

  private resetForm() {
    this.editingVeiculoId.set(null);
    this.errorMessage.set('');
    this.veiculoForm.reset({
      matricula: '',
      placa: '',
      ano: '',
      marca: '',
      modelo: '',
      valor: '',
      taxaJuros: '',
    });
  }
}
