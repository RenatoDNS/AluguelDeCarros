export type VeiculoStatus = 'DISPONIVEL' | 'ALUGADO' | 'VINCULADO';

export type Veiculo = {
  id: number;
  matricula: string;
  placa: string;
  marca: string;
  modelo: string;
  ano: number;
  status: VeiculoStatus;
  valor: number;
  taxaJuros?: number;
  agentId?: number;
  agentType?: 'BANCO' | 'EMPRESA';
};

export type VeiculoCreatePayload = {
  matricula: string;
  placa: string;
  marca: string;
  modelo: string;
  ano: number;
  status: VeiculoStatus;
  valor: number;
  taxaJuros?: number;
};

export type VeiculoUpdatePayload = VeiculoCreatePayload;
