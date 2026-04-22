export type PedidoStatus = 'EM_ANALISE' | 'APROVADO' | 'REJEITADO' | 'CANCELADO';
export type PedidoTipo = 'ALUGUEL' | 'COMPRA';
export type PedidoAvaliacaoResultado = 'APROVADO' | 'REJEITADO';

export type PedidoRequest = {
  clienteId: number;
  automovelId: number;
  dataInicio: string;
  dataFim: string;
  status: PedidoStatus;
};

export type PedidoCreditoRequest = {
  clienteId: number;
  automovelId: number;
  qntdParcelas: number;
};

export type PedidoResponse = {
  id: number;
  numeroProtocolo: string;
  clienteId: number;
  automovelId: number;
  tipoPedido: PedidoTipo;
  dataInicio: string | null;
  dataFim: string | null;
  qntdParcelas: number | null;
  status: PedidoStatus;
  justificativa: string | null;
};

export type PedidoAvaliacaoRequest = {
  resultado: PedidoAvaliacaoResultado;
  justificativa: string;
};
