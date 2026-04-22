import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';

import { finalize } from 'rxjs';

import { type PedidoAvaliacaoResultado, type PedidoResponse } from '../../models/pedido';
import { PedidoService } from '../../services/pedido.service';

type DialogAction = 'APROVADO' | 'REJEITADO';
type PedidoSection = 'em-analise' | 'finalizados';

@Component({
  selector: 'app-pedidos-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, DatePipe],
  templateUrl: './pedidos-page.html',
  styleUrl: './pedidos-page.css',
})
export class PedidosPageComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly pedidoService = inject(PedidoService);

  readonly pedidos = signal<PedidoResponse[]>([]);
  readonly selectedSection = signal<PedidoSection>('em-analise');
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly errorMessage = signal('');
  readonly dialogOpen = signal(false);
  readonly selectedPedido = signal<PedidoResponse | null>(null);
  readonly selectedAction = signal<DialogAction | null>(null);
  readonly avaliacaoForm = this.formBuilder.nonNullable.group({
    justificativa: ['', [Validators.required]],
  });

  constructor() {
    this.loadPedidos();
  }

  setSection(section: PedidoSection) {
    this.selectedSection.set(section);
    this.loadPedidos();
  }

  openDialog(pedido: PedidoResponse, action: DialogAction) {
    this.resetDialogState();
    this.selectedPedido.set(pedido);
    this.selectedAction.set(action);
    this.dialogOpen.set(true);
  }

  closeDialog() {
    this.dialogOpen.set(false);
    this.resetDialogState();
  }

  submit() {
    const pedido = this.selectedPedido();
    const action = this.selectedAction();

    if (!pedido || !action) {
      return;
    }

    if (this.avaliacaoForm.invalid) {
      this.avaliacaoForm.markAllAsTouched();
      return;
    }

    const { justificativa } = this.avaliacaoForm.getRawValue();

    this.saving.set(true);
    this.errorMessage.set('');

    this.pedidoService
      .evaluate(pedido.id, {
        resultado: action as PedidoAvaliacaoResultado,
        justificativa,
      })
      .pipe(finalize(() => this.saving.set(false)))
      .subscribe({
        next: () => {
          this.closeDialog();
          this.loadPedidos();
        },
        error: (error) => {
          this.errorMessage.set(error?.error?.mensagem ?? 'Não foi possível atualizar o pedido.');
        },
      });
  }

  actionLabel() {
    return this.selectedAction() === 'APROVADO' ? 'Aprovação' : 'Cancelamento';
  }

  confirmButtonLabel() {
    return this.selectedAction() === 'APROVADO'
      ? 'Confirmar Aprovação'
      : 'Confirmar Cancelamento';
  }

  private loadPedidos() {
    this.loading.set(true);
    this.errorMessage.set('');

    const request$ =
      this.selectedSection() === 'finalizados'
        ? this.pedidoService.listAgentFinished()
        : this.pedidoService.listAgentPending();

    request$.subscribe({
      next: (pedidos) => {
        this.pedidos.set(pedidos);
        this.loading.set(false);
      },
      error: (error) => {
        this.errorMessage.set(error?.error?.mensagem ?? 'Não foi possível carregar os pedidos.');
        this.loading.set(false);
      },
    });
  }

  private resetDialogState() {
    this.selectedPedido.set(null);
    this.selectedAction.set(null);
    this.errorMessage.set('');
    this.avaliacaoForm.reset({
      justificativa: '',
    });
  }
}
