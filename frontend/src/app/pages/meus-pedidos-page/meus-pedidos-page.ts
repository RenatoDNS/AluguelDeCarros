import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';

import { PedidoResponse, type PedidoStatus } from '../../models/pedido';
import { PedidoService } from '../../services/pedido.service';
import { DatePipe } from '@angular/common';

type StatusMeta = {
  label: string;
  description: string;
  classes: string;
};

@Component({
  selector: 'app-meus-pedidos-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [DatePipe],
  templateUrl: './meus-pedidos-page.html',
  styleUrl: './meus-pedidos-page.css',
})
export class MeusPedidosPageComponent {
  private readonly pedidoService = inject(PedidoService);

  readonly pedidos = signal<PedidoResponse[]>([]);
  readonly loading = signal(true);
  readonly errorMessage = signal('');

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

  statusMeta(status: PedidoStatus) {
    return this.statusMetaMap[status];
  }
}
