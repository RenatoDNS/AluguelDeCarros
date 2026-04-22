export type EmpresaResumo = {
  id: number;
  razaoSocial: string;
  cnpj: string;
  ramoDeAtividade: string;
};

export type BancoResumo = {
  id: number;
  razaoSocial: string;
  cnpj: string;
  codigoBancario: string;
};

export type ClienteResumo = {
  id: number;
  nome: string;
  cpf: string;
  rg: string;
  endereco: string;
  profissao: string;
};

export type VeiculoResumo = {
  id: number;
  matricula: string;
  placa: string;
  ano: number;
  marca: string;
  modelo: string;
  valor: number;
  linkImagem: string;
};

export type ContratoResponse = {
  id: number;
  empresa: EmpresaResumo;
  cliente: ClienteResumo;
  veiculo: VeiculoResumo;
  dataInicioAluguel: string;
  dataFimAluguel: string;
  valorTotal: number;
  valorDiaria: number;
  dataAssinaturaEmpresa: string | null;
  dataAssinaturaCliente: string | null;
  empresaAssinou: boolean;
  clienteAssinou: boolean;
};

export type ContratoCreditoResponse = {
  id: number;
  banco: BancoResumo;
  cliente: ClienteResumo;
  veiculo: VeiculoResumo;
  quantidadeParcelas: number;
  valorParcela: number;
  valorTotal: number;
  dataAssinaturaBanco: string | null;
  dataAssinaturaCliente: string | null;
  bancoAssinou: boolean;
  clienteAssinou: boolean;
};
