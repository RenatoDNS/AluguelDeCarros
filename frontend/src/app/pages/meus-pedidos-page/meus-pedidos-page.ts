import { CurrencyPipe, DatePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  ViewChild,
  inject,
  signal,
} from '@angular/core';
import html2pdf from 'html2pdf.js';

import { type ContratoCreditoResponse, type ContratoResponse } from '../../models/contrato';
import { type PedidoResponse, type PedidoStatus } from '../../models/pedido';
import { ContratoService } from '../../services/contrato.service';
import { PedidoService } from '../../services/pedido.service';

type StatusMeta = {
  label: string;
  description: string;
  classes: string;
};

@Component({
  selector: 'app-meus-pedidos-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [DatePipe, CurrencyPipe],
  templateUrl: './meus-pedidos-page.html',
  styleUrl: './meus-pedidos-page.css',
})
export class MeusPedidosPageComponent {
  private readonly pedidoService = inject(PedidoService);
  private readonly contratoService = inject(ContratoService);

  @ViewChild('contractInfo') private contractInfoRef?: ElementRef<HTMLElement>;

  readonly pedidos = signal<PedidoResponse[]>([]);
  readonly loading = signal(true);
  readonly cancelingPedidoId = signal<number | null>(null);
  readonly errorMessage = signal('');

  readonly dialogOpen = signal(false);
  readonly selectedPedido = signal<PedidoResponse | null>(null);
  readonly loadingContrato = signal(false);
  readonly contratoFetchErrorMessage = signal('');
  readonly signErrorMessage = signal('');
  readonly pdfErrorMessage = signal('');
  readonly contratoAluguel = signal<ContratoResponse | null>(null);
  readonly contratoCredito = signal<ContratoCreditoResponse | null>(null);
  readonly downloadingPdf = signal(false);
  readonly signingContrato = signal(false);

  private readonly statusMetaMap: Record<PedidoStatus, StatusMeta> = {
    EM_ANALISE: {
      label: 'Em análise',
      description: 'Em análise pela equipe',
      classes: 'border-amber-200 bg-amber-50 text-amber-800',
    },
    APROVADO: {
      label: 'Aprovado',
      description: 'Pedido aprovado',
      classes: 'border-emerald-200 bg-emerald-50 text-emerald-800',
    },
    CANCELADO: {
      label: 'Cancelado',
      description: 'Pedido cancelado',
      classes: 'border-slate-200 bg-slate-100 text-slate-700',
    },
    REJEITADO: {
      label: 'Rejeitado',
      description: 'Pedido rejeitado',
      classes: 'border-rose-200 bg-rose-50 text-rose-800',
    },
  };

  constructor() {
    this.loadPedidos();
  }

  canCancel(pedido: PedidoResponse) {
    return pedido.status === 'EM_ANALISE';
  }

  canViewContrato(pedido: PedidoResponse) {
    return pedido.status === 'APROVADO';
  }

  hasContratoLoaded() {
    return Boolean(this.contratoAluguel() || this.contratoCredito());
  }

  assinaturaLabel(assinou: boolean) {
    return assinou ? 'Assinado' : 'Pendente';
  }

  canSignContrato() {
    const pedido = this.selectedPedido();

    if (!pedido) {
      return false;
    }

    if (pedido.tipoPedido === 'ALUGUEL') {
      return this.contratoAluguel()?.clienteAssinou === false;
    }

    return this.contratoCredito()?.clienteAssinou === false;
  }

  cancelPedido(pedidoId: number) {
    this.cancelingPedidoId.set(pedidoId);
    this.errorMessage.set('');

    this.pedidoService.cancel(pedidoId).subscribe({
      next: (pedidoAtualizado) => {
        this.pedidos.update((pedidos) =>
          pedidos.map((pedido) => (pedido.id === pedidoId ? pedidoAtualizado : pedido)),
        );
        this.cancelingPedidoId.set(null);
      },
      error: (error) => {
        this.errorMessage.set(error?.error?.mensagem ?? 'Não foi possível cancelar o pedido.');
        this.cancelingPedidoId.set(null);
      },
    });
  }

  openContratoDialog(pedido: PedidoResponse) {
    this.selectedPedido.set(pedido);
    this.dialogOpen.set(true);
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
    this.dialogOpen.set(false);
    this.selectedPedido.set(null);
    this.loadingContrato.set(false);
    this.contratoFetchErrorMessage.set('');
    this.signErrorMessage.set('');
    this.pdfErrorMessage.set('');
    this.contratoAluguel.set(null);
    this.contratoCredito.set(null);
    this.downloadingPdf.set(false);
    this.signingContrato.set(false);
  }

  signContrato() {
    const pedido = this.selectedPedido();
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
    const pedido = this.selectedPedido();

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

  statusMeta(status: PedidoStatus) {
    return this.statusMetaMap[status];
  }

  private loadPedidos() {
    this.loading.set(true);
    this.errorMessage.set('');

    this.pedidoService.listMine().subscribe({
      next: (pedidos) => {
        this.pedidos.set(pedidos);
        this.loading.set(false);
      },
      error: (error) => {
        this.errorMessage.set(
          error?.error?.mensagem ?? 'Não foi possível carregar os seus pedidos.',
        );
        this.loading.set(false);
      },
    });
  }
}
