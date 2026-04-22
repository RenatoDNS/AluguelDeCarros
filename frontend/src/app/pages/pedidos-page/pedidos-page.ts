import { CurrencyPipe, DatePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  ViewChild,
  inject,
  signal,
} from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import html2pdf from 'html2pdf.js';

import { finalize } from 'rxjs';

import { type UserType } from '../../models/auth';
import { type ContratoCreditoResponse, type ContratoResponse } from '../../models/contrato';
import { type PedidoAvaliacaoResultado, type PedidoResponse } from '../../models/pedido';
import { AuthService } from '../../services/auth.service';
import { ContratoService } from '../../services/contrato.service';
import { PedidoService } from '../../services/pedido.service';

type DialogAction = 'APROVADO' | 'REJEITADO';
type PedidoSection = 'em-analise' | 'finalizados';

@Component({
  selector: 'app-pedidos-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, DatePipe, CurrencyPipe],
  templateUrl: './pedidos-page.html',
  styleUrl: './pedidos-page.css',
})
export class PedidosPageComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly pedidoService = inject(PedidoService);
  private readonly contratoService = inject(ContratoService);
  private readonly authService = inject(AuthService);

  @ViewChild('contractInfo') private contractInfoRef?: ElementRef<HTMLElement>;

  readonly pedidos = signal<PedidoResponse[]>([]);
  readonly selectedSection = signal<PedidoSection>('em-analise');
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly errorMessage = signal('');
  readonly dialogOpen = signal(false);
  readonly selectedPedido = signal<PedidoResponse | null>(null);
  readonly selectedAction = signal<DialogAction | null>(null);

  readonly contratoDialogOpen = signal(false);
  readonly selectedContratoPedido = signal<PedidoResponse | null>(null);
  readonly loadingContrato = signal(false);
  readonly contratoFetchErrorMessage = signal('');
  readonly signErrorMessage = signal('');
  readonly pdfErrorMessage = signal('');
  readonly contratoAluguel = signal<ContratoResponse | null>(null);
  readonly contratoCredito = signal<ContratoCreditoResponse | null>(null);
  readonly downloadingPdf = signal(false);
  readonly signingContrato = signal(false);

  readonly userType = this.authService.userType;

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

  openContratoDialog(pedido: PedidoResponse) {
    this.selectedContratoPedido.set(pedido);
    this.contratoDialogOpen.set(true);
    this.loadingContrato.set(true);
    this.contratoFetchErrorMessage.set('');
    this.signErrorMessage.set('');
    this.pdfErrorMessage.set('');
    this.signingContrato.set(false);
    this.contratoAluguel.set(null);
    this.contratoCredito.set(null);

    this.contratoService.getByPedido(pedido.id, pedido.tipoPedido).subscribe({
      next: (contrato) => {
        if (pedido.tipoPedido === 'ALUGUEL') {
          this.contratoAluguel.set(contrato as ContratoResponse);
        } else {
          this.contratoCredito.set(contrato as ContratoCreditoResponse);
        }
        this.loadingContrato.set(false);
      },
      error: (error) => {
        this.contratoFetchErrorMessage.set(
          error?.error?.mensagem ?? 'Não foi possível carregar as informações do contrato.',
        );
        this.loadingContrato.set(false);
      },
    });
  }

  closeContratoDialog() {
    this.contratoDialogOpen.set(false);
    this.selectedContratoPedido.set(null);
    this.loadingContrato.set(false);
    this.contratoFetchErrorMessage.set('');
    this.signErrorMessage.set('');
    this.pdfErrorMessage.set('');
    this.contratoAluguel.set(null);
    this.contratoCredito.set(null);
    this.downloadingPdf.set(false);
    this.signingContrato.set(false);
  }

  hasContratoLoaded() {
    return Boolean(this.contratoAluguel() || this.contratoCredito());
  }

  assinaturaLabel(assinou: boolean) {
    return assinou ? 'Assinado' : 'Pendente';
  }

  canSignContrato() {
    const pedido = this.selectedContratoPedido();
    const userType = this.userType();

    if (!pedido || !userType) {
      return false;
    }

    if (pedido.tipoPedido === 'ALUGUEL') {
      return userType === 'empresa' && this.contratoAluguel()?.empresaAssinou === false;
    }

    return userType === 'banco' && this.contratoCredito()?.bancoAssinou === false;
  }

  signContrato() {
    const pedido = this.selectedContratoPedido();
    const contrato = pedido?.tipoPedido === 'ALUGUEL' ? this.contratoAluguel() : this.contratoCredito();

    if (!pedido || !contrato || this.signingContrato()) {
      return;
    }

    this.signingContrato.set(true);
    this.signErrorMessage.set('');

    this.contratoService.sign(contrato.id, pedido.tipoPedido).subscribe({
      next: (contratoAtualizado) => {
        if (pedido.tipoPedido === 'ALUGUEL') {
          this.contratoAluguel.set(contratoAtualizado as ContratoResponse);
        } else {
          this.contratoCredito.set(contratoAtualizado as ContratoCreditoResponse);
        }
        this.signingContrato.set(false);
      },
      error: (error) => {
        this.signErrorMessage.set(error?.error?.mensagem ?? 'Não foi possível assinar o contrato.');
        this.signingContrato.set(false);
      },
    });
  }

  async downloadContratoPdf() {
    const element = this.contractInfoRef?.nativeElement;
    const pedido = this.selectedContratoPedido();

    if (!element || !pedido) {
      return;
    }

    this.downloadingPdf.set(true);
    this.pdfErrorMessage.set('');

    try {
      await html2pdf()
        .set({
          filename: `contrato-${pedido.numeroProtocolo}.pdf`,
          margin: 10,
          image: { type: 'jpeg', quality: 0.98 },
          html2canvas: { scale: 2 },
          jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' },
        })
        .from(element)
        .save();
    } catch {
      this.pdfErrorMessage.set('Não foi possível gerar o PDF do contrato.');
    } finally {
      this.downloadingPdf.set(false);
    }
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
