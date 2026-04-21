import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { NgxMaskDirective } from 'ngx-mask';
import { finalize } from 'rxjs';

import { type Veiculo } from '../../models/veiculo';
import { VeiculoService } from '../../services/veiculo.service';

@Component({
  selector: 'app-meus-veiculos-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './meus-veiculos-page.html',
  styleUrl: './meus-veiculos-page.css',
})
export class MeusVeiculosPageComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly veiculoService = inject(VeiculoService);

  readonly veiculos = signal<Veiculo[]>([]);
  readonly dialogOpen = signal(false);
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly deletingVeiculoId = signal<number | null>(null);
  readonly editingVeiculoId = signal<number | null>(null);
  readonly errorMessage = signal('');
  readonly veiculoForm = this.formBuilder.nonNullable.group({
    matricula: ['', [Validators.required]],
    placa: ['', [Validators.required, Validators.maxLength(7)]],
    ano: ['', [Validators.required, Validators.pattern(/^\d+$/)]],
    marca: ['', [Validators.required]],
    modelo: ['', [Validators.required]],
    diaria: ['', [Validators.required]],
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
      diaria: this.formatCurrencyValue(veiculo.diaria),
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

    const { matricula, placa, ano, marca, modelo, diaria } = this.veiculoForm.getRawValue();
    const diariaValue = this.parseCurrencyValue(diaria);

    if (!Number.isFinite(diariaValue) || diariaValue <= 0) {
      this.errorMessage.set('Informe uma diária válida.');
      return;
    }

    this.saving.set(true);
    this.errorMessage.set('');

    const request$ = this.editingVeiculoId()
      ? this.veiculoService.update(this.editingVeiculoId()!, {
          matricula,
          placa,
          ano: Number(ano),
          marca,
          modelo,
          status: 'DISPONIVEL',
          diaria: diariaValue,
        })
      : this.veiculoService.create({
          matricula,
          placa,
          ano: Number(ano),
          marca,
          modelo,
          status: 'DISPONIVEL',
          diaria: diariaValue,
        });

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
      diaria: '',
    });
  }
}
