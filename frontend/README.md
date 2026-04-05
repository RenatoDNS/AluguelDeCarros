# 🚗 Aluguel de Carros — Frontend (Web)

> Aplicação Web em **Angular** para gestão de clientes, autenticação e integração com a API **Micronaut** de aluguel de carros.

<table>
  <tr>
    <td width="800px">
      <div align="justify">
        Este <b>README.md</b> descreve o <b>Frontend</b> do projeto <b>Aluguel de Carros</b>, uma aplicação web moderna desenvolvida com <b>Angular</b> que oferece interface amigável para cadastro, autenticação e gestão de clientes. Integra-se com a API REST backend via serviços HTTP, com autenticação JWT e proteção de rotas via guards.
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
- **Autenticação JWT** — login seguro com CPF/login e armazenamento de token
- **Gestão de clientes** — cadastro, visualização, edição e exclusão
- **Proteção de rotas** — guards que validam autenticação antes de acesso
- **Integração com API REST** — consumo dos endpoints do backend Micronaut
- **Design moderno** — experiência de usuário clara e intuitiva

**Por que existe:** oferecer uma interface amigável e segura para interação com o sistema de aluguel de carros.

**Problema que resolve:** facilita o acesso às funcionalidades da API de forma visual e intuitiva.

**Contexto:** projeto acadêmico na PUC Minas focado em arquitetura moderna de aplicações web com Angular.

---

## ✨ Funcionalidades Principais

- 🔐 **Login Seguro** — autenticação via CPF ou login com validação JWT
- 📋 **Cadastro de Clientes** — formulário com validação de dados pessoais e entidades empregadoras
- 👁️ **Visualização de Clientes** — listagem e detalhes de clientes cadastrados
- ✏️ **Edição de Clientes** — atualização de dados pessoais (com autenticação)
- 🗑️ **Exclusão de Clientes** — remoção segura com confirmação
- 🔒 **Proteção de Rotas** — acesso restrito a usuários autenticados via guards
- 💾 **Persistência de Sessão** — armazenamento seguro de token JWT no localStorage
- 📱 **Design Responsivo** — interface adaptável para desktop, tablet e mobile

---

## 🛠 Tecnologias Utilizadas

### 💻 Front-end

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| **Angular** | 21.2.6 | Framework web e componentes |
| **TypeScript** | 5.x | Linguagem tipada |
| **Angular Router** | 21.x | Roteamento entre páginas |
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

1. **Pages** (`pages/`) — Componentes de páginas: login, dashboard, cadastro de clientes
2. **Services** (`services/`) — `AuthService` (autenticação), `ClienteService` (CRUD)
3. **Guards** (`guards/`) — `AuthGuard` (proteção de rotas autenticadas)
4. **Routes** (`app.routes.ts`) — Definição de rotas e lazy loading
5. **Components** — Componentes reutilizáveis (headers, forms, etc.)

**Padrões aplicados:**
- **Smart/Presentational Components** — separação de lógica e apresentação
- **Reactive Programming (RxJS)** — observables para estado e eventos
- **HttpClient** — consumo da API REST
- **Route Guards** — proteção de rotas autenticadas
- **State Management** — gerenciamento simples via serviços injetáveis

---

## 🔧 Instalação e Execução

### Pré-requisitos

- **Node.js** — Versão LTS 20.x ou superior
- **npm** — Versão 10.x (incluído com Node.js)
- **Angular CLI** — Será instalado como dependência do projeto

---

### 🔑 Variáveis de Ambiente

Crie um arquivo **`.env`** na raiz da pasta `frontend/`:

```env
# URL da API Backend (desenvolvimento local)
VITE_API_URL=http://localhost:8080/api/aluguelcarros/v1
```

**Para produção**, configure a variável no seu provedor (Vercel, Netlify, etc.).

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
│   └── favicon.ico                     # 🎨 Ícone da aplicação
│
├── src/
│   ├── index.html                      # 📄 Arquivo HTML principal
│   ├── main.ts                         # 🚀 Entry point da aplicação
│   ├── styles.css                      # 🎨 Estilos globais
│   │
│   └── app/
│       ├── app.ts                      # 🎯 Componente raiz
│       ├── app.html                    # 🏗️ Template do app
│       ├── app.css                     # 🎨 Estilos do app
│       ├── app.routes.ts               # 🛣️ Definição de rotas
│       ├── app.config.ts               # ⚙️ Configuração da aplicação
│       │
│       ├── guards/                     # 🔒 Proteção de rotas
│       │   └── auth.guard.ts           # Valida autenticação
│       │
│       ├── pages/                      # 📄 Páginas da aplicação
│       │   ├── dashboard-page/
│       │   ├── login-page/
│       │   ├── cliente-form-page/
│       │   └── cliente-list-page/
│       │
│       ├── services/                   # 🔌 Serviços HTTP
│       │   ├── auth.service.ts         # Autenticação e JWT
│       │   └── cliente.service.ts      # CRUD de clientes
│       │
│       └── components/                 # 🧱 Componentes reutilizáveis
│           ├── header/
│           ├── footer/
│           └── shared/
│
├── environments/
│   └── environment.ts                  # 🌍 Configuração de ambiente
│
└── .editorconfig                       # ✍️ Padronização de código
```

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
