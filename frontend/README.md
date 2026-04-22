# 🚗 Aluguel de Carros — Frontend (Web)

> Aplicação Web em **Angular** para autenticação, descoberta de veículos, gestão de pedidos e assinatura de contratos, integrada à API **Micronaut** de aluguel de carros.

<table>
  <tr>
    <td width="800px">
      <div align="justify">
        Este <b>README.md</b> descreve o <b>Frontend</b> do projeto <b>Aluguel de Carros</b>, uma aplicação web moderna desenvolvida com <b>Angular</b> que oferece interface amigável para cadastro, autenticação e gestão de pedidos e contratos. Integra-se com a API REST backend via serviços HTTP, com autenticação JWT armazenada em <b>sessionStorage</b> e proteção de rotas via guards de autenticação e perfil.
      </div>
    </td>
    <td>
      <div align="center">🌐</div>
    </td>
  </tr>
</table>

---

## 🚧 Status do Projeto

[![Versão](https://img.shields.io/badge/versão-0.1-blue)](package.json)
[![Angular](https://img.shields.io/badge/Angular-21.2.6-dd0031?logo=angular&logoColor=white)](package.json)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.x-3178c6?logo=typescript&logoColor=white)](package.json)
[![Licença](https://img.shields.io/badge/licença-MIT-green)](../LICENSE)

---

## 📚 Índice

- [Links Úteis](#-links-úteis)
- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Instalação e Execução](#-instalação-e-execução)
  - [Pré-requisitos](#pré-requisitos)
  - [Variáveis de Ambiente](#-variáveis-de-ambiente)
  - [Instalação de Dependências](#-instalação-de-dependências)
  - [Como Executar a Aplicação](#-como-executar-a-aplicação)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [Testes](#-testes)
- [Build para Produção](#-build-para-produção)
- [Documentações utilizadas](#-documentações-utilizadas)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

---

## 🔗 Links Úteis

| Recurso | URL / Caminho |
|---------|---------------|
| **API Backend** | `http://localhost:8080/api/aluguelcarros/v1` |
| **Documentação Backend** | [`../backend/README.md`](../backend/README.md) |
| **Documentação Técnica** | [`../docs/`](../docs/) — diagramas UML e especificações |
| **Angular CLI Reference** | [Documentação Angular](https://angular.dev/tools/cli) |

---

## 📝 Sobre o Projeto

O **Frontend do Aluguel de Carros** é uma aplicação web desenvolvida com **Angular** que fornece:

- **Interface responsiva** — acesso via desktop e dispositivos móveis
- **Autenticação JWT** — login e cadastro com armazenamento de token em sessionStorage
- **Descoberta de veículos** — catálogo de automóveis disponíveis para aluguel ou compra a crédito
- **Gestão de pedidos** — criação, acompanhamento e cancelamento para clientes; avaliação para agentes
- **Assinatura de contratos** — fluxo de assinatura de contratos de aluguel e crédito
- **Painel por perfil** — dashboards diferenciados para clientes e agentes (empresa/banco)
- **Proteção de rotas** — guards que validam autenticação e tipo de perfil antes do acesso

**Por que existe:** oferecer uma interface amigável e segura para interação com o sistema de aluguel de carros.

**Problema que resolve:** facilita o acesso às funcionalidades da API de forma visual e intuitiva.

**Contexto:** projeto acadêmico na PUC Minas focado em arquitetura moderna de aplicações web com Angular.

---

## ✨ Funcionalidades Principais

- 🔐 **Autenticação** — login e cadastro (cliente, empresa ou banco) com validação JWT
- 🚗 **Descobrir Veículos** — catálogo de automóveis disponíveis com filtros (somente perfil `cliente`)
- 📋 **Meus Pedidos** — listagem, acompanhamento de status e cancelamento de pedidos (somente `cliente`)
- 📄 **Assinatura de Contratos** — visualização e assinatura de contratos de aluguel e crédito diretamente nos pedidos aprovados
- 🏢 **Meus Veículos** — cadastro, edição e remoção de automóveis pelo agente autenticado (`empresa` ou `banco`)
- 📥 **Pedidos do Agente** — listagem de pedidos em análise e histórico de aprovados/rejeitados, com avaliação inline
- 🔒 **Proteção de Rotas** — `AuthGuard` impede acesso sem autenticação; `RoleGuard` restringe páginas por tipo de perfil
- 💾 **Persistência de Sessão** — token JWT armazenado em `sessionStorage` (limpo ao fechar o navegador)

---

## 🛠 Tecnologias Utilizadas

### 💻 Front-end

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| **Angular** | 21.2.6 | Framework web e componentes |
| **TypeScript** | 5.x | Linguagem tipada |
| **Angular Router** | 21.x | Roteamento entre páginas e lazy loading |
| **Angular Forms** | 21.x | Formulários reativos |
| **RxJS** | 7.x | Programação reativa com observables |
| **HttpClient** | 21.x | Requisições HTTP para a API |
| **Node.js** | 20.x LTS | Runtime para desenvolvimento |
| **npm** | 10.x | Gerenciador de dependências |

---

## 🏗 Arquitetura

A aplicação segue a **arquitetura em componentes com serviços**:

```
┌──────────────────────────┐
│   Routes / Router        │  → Navegação entre páginas
├──────────────────────────┤
│   Pages / Components     │  → Componentes UI
├──────────────────────────┤
│   Services               │  → Chamadas HTTP e lógica
├──────────────────────────┤
│   Guards                 │  → Proteção de rotas
├──────────────────────────┤
│   HttpClient             │  → Comunicação com API
└──────────────────────────┘
```

### Componentes Principais

1. **Pages** (`pages/`) — Componentes de páginas com lazy loading:
   - `auth-page` — Login e cadastro (cliente, empresa ou banco)
   - `dashboard-page` — Shell com menu lateral e roteamento interno
   - `dashboard-home-page` — Tela inicial do dashboard
   - `descobrir-page` — Catálogo de veículos para clientes
   - `meus-pedidos-page` — Pedidos e contratos do cliente
   - `meus-veiculos-page` — Gestão de veículos pelo agente
   - `pedidos-page` — Avaliação de pedidos pelo agente
2. **Services** (`services/`) — `AuthService` (autenticação e sessão), `VeiculoService` (CRUD de automóveis), `PedidoService` (pedidos e avaliações), `ContratoService` (busca e assinatura de contratos)
3. **Guards** (`guards/`) — `authGuard` (exige autenticação), `roleGuard` (restringe por `userType`)
4. **Components** (`components/`) — `veiculo-card` (card reutilizável de exibição de veículo)
5. **Models** (`models/`) — Tipos TypeScript: `auth`, `veiculo`, `pedido`, `contrato`
6. **Routes** (`app.routes.ts`) — Definição de rotas com lazy loading e guards

**Padrões aplicados:**
- **Smart/Presentational Components** — separação de lógica e apresentação
- **Reactive Programming (RxJS)** — observables para estado e eventos
- **HttpClient** — consumo da API REST
- **Route Guards** — proteção de rotas por autenticação e perfil
- **State Management** — gerenciamento simples via serviços injetáveis e Angular Signals

---

## 🔧 Instalação e Execução

### Pré-requisitos

- **Node.js** — Versão LTS 20.x ou superior
- **npm** — Versão 10.x (incluído com Node.js)
- **Angular CLI** — Será instalado como dependência do projeto

---

### 🔑 Variáveis de Ambiente

A URL da API é configurada diretamente em `src/environments/environment.ts`:

```typescript
export const environment = {
  apiUrl: 'http://localhost:8080/api/aluguelcarros/v1'
};
```

**Para produção**, crie o arquivo `src/environments/environment.prod.ts` com a URL do servidor de produção e configure o build no `angular.json`.

---

### 📦 Instalação de Dependências

```bash
cd frontend
npm install
cd ..
```

---

### ⚡ Como Executar a Aplicação

**Iniciar o servidor de desenvolvimento:**

```bash
cd frontend
npm start
```

ou

```bash
ng serve
```

✅ **O frontend estará disponível em:**
- `http://localhost:4200`

A aplicação **recarrega automaticamente** quando você modifica os arquivos.

---

## 📂 Estrutura de Pastas

```
frontend/
├── package.json                        # 📦 Dependências e scripts npm
├── angular.json                        # ⚙️ Configuração do Angular CLI
├── tsconfig.json                       # 🔧 Configuração TypeScript
├── tsconfig.app.json                   # 📐 TypeScript para app
├── tsconfig.spec.json                  # 🧪 TypeScript para testes
│
├── public/
│   ├── favicon.ico                     # 🎨 Ícone da aplicação
│   ├── logo_lab.jpeg                   # 🖼️ Logo do laboratório
│   └── red-sedan.png                   # 🚗 Imagem padrão de veículo
│
├── src/
│   ├── index.html                      # 📄 Arquivo HTML principal
│   ├── main.ts                         # 🚀 Entry point da aplicação
│   ├── styles.css                      # 🎨 Estilos globais
│   │
│   ├── environments/
│   │   └── environment.ts              # 🌍 Configuração de ambiente (apiUrl)
│   │
│   └── app/
│       ├── app.ts                      # 🎯 Componente raiz
│       ├── app.html                    # 🏗️ Template do app
│       ├── app.css                     # 🎨 Estilos do app
│       ├── app.routes.ts               # 🛣️ Definição de rotas
│       ├── app.config.ts               # ⚙️ Configuração da aplicação
│       │
│       ├── guards/                     # 🔒 Proteção de rotas
│       │   ├── auth.guard.ts           # Exige autenticação
│       │   └── role.guard.ts           # Restringe por userType
│       │
│       ├── interceptors/               # 🔗 Interceptores HTTP
│       │   ├── auth.interceptor.ts     # Injeta Bearer token nas requisições
│       │   └── auth-error.interceptor.ts # Trata erros 401
│       │
│       ├── pages/                      # 📄 Páginas da aplicação
│       │   ├── auth-page/              # Login e cadastro
│       │   ├── dashboard-page/         # Shell do dashboard
│       │   ├── dashboard-home-page/    # Tela inicial
│       │   ├── descobrir-page/         # Catálogo de veículos (cliente)
│       │   ├── meus-pedidos-page/      # Pedidos e contratos (cliente)
│       │   ├── meus-veiculos-page/     # Gestão de veículos (agente)
│       │   └── pedidos-page/           # Avaliação de pedidos (agente)
│       │
│       ├── services/                   # 🔌 Serviços HTTP
│       │   ├── auth.service.ts         # Autenticação e sessão JWT
│       │   ├── veiculo.service.ts      # CRUD de automóveis
│       │   ├── pedido.service.ts       # Pedidos e avaliações
│       │   └── contrato.service.ts     # Busca e assinatura de contratos
│       │
│       ├── models/                     # 📐 Tipos TypeScript
│       │   ├── auth.ts                 # Login, registro, userType
│       │   ├── veiculo.ts              # Veículo e payloads
│       │   ├── pedido.ts               # Pedido e payloads
│       │   └── contrato.ts             # Contrato e contrato de crédito
│       │
│       └── components/                 # 🧱 Componentes reutilizáveis
│           └── veiculo-card/           # Card de exibição de veículo
│
└── .editorconfig                       # ✍️ Padronização de código
```

---

## 🧪 Testes

```bash
cd frontend
npm test
```

ou

```bash
ng test
```

---

## 🏗️ Build para Produção

```bash
cd frontend
npm run build
```

ou

```bash
ng build
```

Os artefatos serão gerados em `dist/`.

---

## 🔗 Documentações utilizadas

- [**Angular Documentation**](https://angular.dev/) — Framework e componentes
- [**Angular Router**](https://angular.dev/guide/router) — Roteamento e guards
- [**Angular Forms**](https://angular.dev/guide/forms) — Formulários reativos e validação
- [**RxJS**](https://rxjs.dev/) — Observables e operadores
- [**TypeScript Handbook**](https://www.typescriptlang.org/docs/) — Tipagem e tipos
- [**Conventional Commits**](https://www.conventionalcommits.org/) — Padrão de mensagens

---

## 🤝 Contribuição

1. Faça um *fork* do repositório.
2. Crie uma branch (`git checkout -b feature/minha-feature`).
3. Commit com mensagens claras ([Conventional Commits](https://www.conventionalcommits.org/)).
4. Abra um *Pull Request* descrevendo as mudanças.

---

## 📄 Licença

Este projeto está licenciado sob a **MIT License** — veja o arquivo [LICENSE](../LICENSE).

---

**Desenvolvido com ❤️ — [Voltar ao README Principal](../README.md)**