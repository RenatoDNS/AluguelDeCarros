export type LoginPayload = {
  login: string;
  password: string;
};

export type LoginRequest = {
  login: string;
  senha: string;
};

export type UserType = 'cliente' | 'empresa' | 'banco';

export type LoginResponse = {
  token: string;
  expiresIn: number;
  userType: UserType;
};

export type AuthMeResponse = {
  id: number;
  login: string;
  userType: UserType;
};

export type EntidadeEmpregadoraPayload = {
  nomeEmpresa: string;
  cnpj: string;
  rendimento: number;
};

export type ClienteRegisterPayload = {
  rg: string;
  cpf: string;
  nome: string;
  endereco: string;
  profissao: string;
  senha: string;
  entidadesEmpregadoras: EntidadeEmpregadoraPayload[];
};

export type EmpresaRegisterPayload = {
  razaoSocial: string;
  cnpj: string;
  ramoDeAtividade: string;
  senha: string;
};

export type BancoRegisterPayload = {
  razaoSocial: string;
  cnpj: string;
  codigoBancario: string;
  senha: string;
};

export type RegisterPayload =
  | ClienteRegisterPayload
  | EmpresaRegisterPayload
  | BancoRegisterPayload;
