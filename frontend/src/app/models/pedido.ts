export type PedidoStatus = 'EM_ANALISE' | 'APROVADO' | 'REJEITADO' | 'CANCELADO';
export type PedidoAvaliacaoResultado = 'APROVADO' | 'REJEITADO';

export type PedidoRequest = {
  clienteId: number;
  automovelId: number;
  dataInicio: string;
  dataFim: string;
  status: PedidoStatus;
};

export type PedidoResponse = {
  id: number;
  numeroProtocolo: string;
  clienteId: number;
  automovelId: number;
  dataInicio: string;
  dataFim: string;
  status: PedidoStatus;
};

export type PedidoAvaliacaoRequest = {
  resultado: PedidoAvaliacaoResultado;
  justificativa: string;
};
