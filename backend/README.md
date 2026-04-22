# 🚗 Aluguel de Carros — API REST (Backend)

> API REST em **Micronaut** para autenticação, CRUD completo das entidades de domínio e fluxos de negócio, com integração ao banco **PostgreSQL** e documentação **OpenAPI/Swagger**.

<table>
  <tr>
    <td width="800px">
      <div align="justify">
        Este <b>README.md</b> descreve o <b>Backend</b> do projeto <b>Aluguel de Carros</b>, uma API REST que implementa autenticação via JWT, CRUD completo de clientes, empresas, bancos, automóveis, pedidos e pareceres de agente, além de contratos de aluguel e contratos de crédito gerados automaticamente pelo fluxo de aprovação. Segue uma arquitetura em camadas (controller, facade, service, repository) com persistência via JPA/Hibernate. A documentação interativa fica disponível via <b>Swagger UI</b> em <code>src/main/resources/swagger.yml</code>.
      </div>
    </td>
    <td>
      <div align="center">⚙️</div>
    </td>
  </tr>
</table>

---

## 🚧 Status do Projeto

[![Versão](https://img.shields.io/badge/versão-0.1-blue)](pom.xml)
[![Java](https://img.shields.io/badge/Java-21-437291?logo=openjdk&logoColor=white)](pom.xml)
[![Micronaut](https://img.shields.io/badge/Micronaut-4.10-007ec6)](pom.xml)
[![Licença](https://img.shields.io/badge/licença-MIT-green)](../LICENSE)

---

## 📚 Índice

- [Links Úteis](#-links-úteis)
- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Autenticação por Perfil](#-autenticação-por-perfil)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Instalação e Execução](#-instalação-e-execução)
  - [Pré-requisitos](#pré-requisitos)
  - [Variáveis de Ambiente](#-variáveis-de-ambiente)
  - [Como Executar a Aplicação](#-como-executar-a-aplicação)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [Testes](#-testes)
- [Documentações utilizadas](#-documentações-utilizadas)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

---

## 🔗 Links Úteis

| Recurso                  | Caminho / URL                                                           |
| ------------------------ | ----------------------------------------------------------------------- |
| **OpenAPI (YAML)**       | [`src/main/resources/swagger.yml`](src/main/resources/swagger.yml)      |
| **Swagger UI**           | `http://localhost:8080/api/aluguelcarros/v1/swagger-ui`                 |
| **Documentação Técnica** | [`../docs/`](../docs/) — diagramas UML (casos de uso e classes/pacotes) |

---

## 📝 Sobre o Projeto

O **Backend do Aluguel de Carros** é uma API HTTP desenvolvida com **Micronaut** que oferece:

- **Gestão das entidades de domínio** — clientes, empresas, bancos, automóveis, pedidos, contratos e contratos de crédito.
- **Autenticação JWT** — emissão e validação de tokens para proteção de rotas sensíveis.
- **API REST bem definida** — endpoints documentados via OpenAPI/Swagger com tratamento padronizado de erros.
- **Persistência com JPA/Hibernate** — usando PostgreSQL como banco padrão.

**Por que existe:** oferece um serviço HTTP seguro e bem documentado para integração com front-ends e outros sistemas.

**Problema que resolve:** padroniza a comunicação entre cliente e servidor via REST, com autenticação robusta e documentação automática.

**Contexto:** projeto acadêmico na PUC Minas focado em boas práticas de engenharia de software e arquitetura em camadas.

---

## ✨ Funcionalidades Principais

- 🔐 **Cadastro Público de Cliente** — `POST /clientes` (sem autenticação); inclui lista de 1 a 3 entidades empregadoras.
- 🔑 **Autenticação JWT por Perfil** — `POST /auth/login` por CPF (cliente) ou CNPJ (empresa/banco), com retorno de `userType`.
- 👤 **Perfil Autenticado** — `GET /auth/me` retorna `id`, `login` e `userType` a partir do token.
- 📋 **CRUDs completos** — `clientes`, `bancos`, `empresas`, `automoveis` (com filtro por status e listagem por agente autenticado).
- 🚗 **Fluxo de Pedidos** — criação de pedidos de aluguel (`POST /pedidos/aluguel`) e de compra a crédito (`POST /pedidos/credito`); listagem pelo cliente autenticado (`GET /pedidos/me`) e cancelamento (`POST /pedidos/{id}/cancelar`).
- 🏢 **Fluxo do Agente** — listagem de pedidos por status vinculados ao agente (`GET /agente/pedidos/{status}`) e avaliação de pedido com aprovação ou rejeição (`POST /agente/pedidos/{id}/avaliar`).
- 📄 **Contratos** — gerados automaticamente ao aprovar pedido de aluguel; consulta por id do pedido (`GET /contratos/{pedidoId}`) e assinatura por empresa ou cliente (`POST /contratos/{id}/assinar`).
- 💳 **Contratos de Crédito** — gerados automaticamente ao aprovar pedido de compra; consulta por id do pedido (`GET /contratos-credito/{pedidoId}`) e assinatura por banco ou cliente (`POST /contratos-credito/{id}/assinar`).
- ✔️ **Validação de Regras de Negócio** — CPF/CNPJ/placa/matrícula únicos, limites de entidades empregadoras, status e regras de transição de fluxo.
- 📊 **Tratamento Robusto de Erros** — respostas JSON com mensagens claras para 404 (não encontrado) e 422 (regra de negócio).
- 🔒 **Segurança em Rotas Protegidas** — 401 quando token inválido ou ausente.
- 📖 **Documentação Automática** — Swagger UI e contrato OpenAPI completo em YAML.

---

## 🔐 Autenticação por Perfil

- **Login único** — `POST /auth/login` com `login` e `senha`.
- **Cliente** — autentica por **CPF** e recebe `userType: cliente`.
- **Empresa** — autentica por **CNPJ** e recebe `userType: empresa`.
- **Banco** — autentica por **CNPJ** e recebe `userType: banco`.
- **Perfil autenticado** — `GET /auth/me` retorna `id`, `login` e `userType` do token.
- **Rotas públicas atuais** — `POST /clientes`, `POST /bancos`, `POST /empresas`, `POST /auth/login` e `GET /swagger-ui`.

---

## 🛠 Tecnologias Utilizadas

### 🖥️ Back-end

| Tecnologia            | Versão | Uso                                     |
| --------------------- | ------ | --------------------------------------- |
| **Java**              | 21     | Linguagem de programação                |
| **Micronaut**         | 4.10   | Framework HTTP, DI, validação           |
| **Maven**             | 3.9+   | Build e gerenciador de dependências     |
| **PostgreSQL**        | 16+    | Banco de dados relacional               |
| **JPA / Hibernate**   | Latest | Mapeamento objeto-relacional            |
| **JJWT**              | Latest | Geração e validação de tokens JWT       |
| **BCrypt (favre)**    | Latest | Hash seguro de senhas                   |
| **Lombok**            | Latest | Redução de boilerplate                  |
| **micronaut-openapi** | Latest | Swagger UI e OpenAPI                    |

---

## 🏗 Arquitetura

A aplicação segue um **padrão monolítico em camadas**:

```
┌──────────────────────────┐
│   Controllers (REST)     │  → Endpoints HTTP
├──────────────────────────┤
│   Facade                 │  → Orquestra DTOs ↔ Entidades
├──────────────────────────┤
│   Services               │  → Lógica e Regras de Negócio
├──────────────────────────┤
│   Repositories (JPA)     │  → Acesso a Dados
├──────────────────────────┤
│   Models (Entidades)     │  → Mapeamento para BD
└──────────────────────────┘
```

### Componentes Principais

1. **Controllers** (`controller/`) — Endpoints REST (`/auth`, `/clientes`, `/bancos`, `/empresas`, `/automoveis`, `/pedidos`, `/agente/pedidos`, `/contratos`, `/contratos-credito`), validação de entrada.
2. **Facade** (`facade/`) — Conversão DTOs ↔ Entidades, orquestração entre camadas.
3. **Services** (`service/`) — Regras de autenticação, lógica de negócio, transações.
4. **Repositories** (`repository/`) — Queries JPA, acesso a dados com Micronaut Data.
5. **Models** (`model/`) — Entidades JPA persistentes no banco.
6. **DTOs** (`dto/`) — Objetos de transferência de dados (request/response).
7. **Config** (`config/`) — `JwtConfig`, `SecurityConfig`, `JwtAuthenticationFilter`.
8. **Exception** (`exception/`) — Exceções de domínio e handler global.

**Padrões aplicados:**

- **Repository Pattern** — isolamento de persistência
- **Service Layer** — concentração de regras de negócio
- **DTO Pattern** — separação entre entrada/saída e entidades internas
- **Exception Handling Global** — tratamento padronizado de erros
- **Injeção de Dependências (Micronaut)** — gerenciamento automático de beans

---

## 🔧 Instalação e Execução

### Pré-requisitos

**Para execução via Docker (recomendado):**
- **Docker** e **Docker Compose** — toda a stack (API + banco) sobe com um único comando.

**Para execução local (sem Docker):**
- **JDK 21** — conforme especificado em `pom.xml`
- **Maven** (opcional) — o projeto inclui **Maven Wrapper** (`mvnw` / `mvnw.bat`)
- **PostgreSQL 16+** — instância acessível na porta `5432`

---

### 🔑 Variáveis de Ambiente

Todas as configurações da aplicação são injetadas via variáveis de ambiente. O arquivo `.env.example` na pasta `backend/` contém os valores padrão para desenvolvimento local:

```env
DB_URL=jdbc:postgresql://localhost:5432/aluguelcarros
DB_USERNAME=postgres
DB_PASSWORD=postgres

JWT_SECRET=troque-este-valor-por-um-segredo-forte
JWT_EXPIRATION_MS=3600000

SERVER_CONTEXT_PATH=/api/aluguelcarros/v1
SERVER_PORT=8080

CORS_ENABLED=true
CORS_ALLOWED_ORIGIN=http://localhost:4200
CORS_ALLOW_CREDENTIALS=true
```

> ⚠️ **Nunca versione segredos reais.** Em produção, substitua `JWT_SECRET` por um valor forte e gerado aleatoriamente.

---

### ⚡ Como Executar a Aplicação

#### 🐳 Opção 1 — Docker (recomendado)

O `docker-compose.yml` dentro de `backend/` sobe **API + PostgreSQL** juntos, sem nenhuma instalação local de Java ou banco.

```bash
cd backend
docker compose up -d --build
```

O Docker Compose usa `.env.example` como fonte de variáveis e substitui automaticamente `DB_URL` pelo hostname interno do container (`postgres`). Para encerrar:

```bash
docker compose down
```

Para remover também o volume de dados do banco:

```bash
docker compose down -v
```

---

#### 💻 Opção 2 — Local (sem Docker)

Requer **JDK 21** e uma instância **PostgreSQL 16+** rodando localmente.

1. Copie o arquivo de variáveis e ajuste se necessário:

```bash
cp .env.example .env
```

2. Exporte as variáveis e execute:

**Linux / macOS:**

```bash
export $(grep -v '^#' .env | xargs)
./mvnw mn:run
```

**Windows (PowerShell):**

```powershell
Get-Content .env.example | ForEach-Object {
  if ($_ -match "^\s*([^#][^=]*)=(.*)$") {
    [System.Environment]::SetEnvironmentVariable($matches[1].Trim(), $matches[2].Trim())
  }
}
.\mvnw.bat mn:run
```

---

✅ **A API ficará disponível em:**

- **Base:** `http://localhost:8080`
- **Context Path:** `/api/aluguelcarros/v1`
- **Exemplo:** `http://localhost:8080/api/aluguelcarros/v1/auth/login`

✅ **Swagger UI disponível em:**

- `http://localhost:8080/api/aluguelcarros/v1/swagger-ui`

---

## 📂 Estrutura de Pastas

```
backend/
├── pom.xml                          # 🛠️ Maven: dependências e build
├── mvnw / mvnw.bat                  # ⚙️ Maven Wrapper (Unix / Windows)
├── Dockerfile                       # 🐳 Imagem Docker multi-stage (build + JRE)
├── docker-compose.yml               # 🐳 Stack completa: API + PostgreSQL
├── .env.example                     # 🔑 Variáveis de ambiente (modelo)
├── micronaut-cli.yml                # 🔧 Configuração Micronaut CLI
├── aot-jar.properties               # ⚙️ Propriedades AOT
│
├── src/main/
│   ├── java/br/pucminas/aluguelcarros/
│   │   ├── Application.java                 # 🚀 Aplicação principal (entry point)
│   │   │
│   │   ├── config/                          # 🔧 Configurações da aplicação
│   │   │   ├── JwtConfig.java               # JWT configuration
│   │   │   ├── JwtAuthenticationFilter.java # Filtro global de Bearer JWT
│   │   │   └── SecurityConfig.java          # Rotas públicas e protegidas
│   │   │
│   │   ├── controller/                      # 🎮 REST Endpoints
│   │   │   ├── AuthController.java
│   │   │   ├── ClienteController.java
│   │   │   ├── BancoController.java
│   │   │   ├── EmpresaController.java
│   │   │   ├── AutomovelController.java
│   │   │   ├── PedidoController.java
│   │   │   ├── AgentePedidoController.java
│   │   │   ├── ContratoController.java
│   │   │   ├── ContratoCreditoController.java
│   │   │   └── SwaggerController.java
│   │   │
│   │   ├── facade/                          # 🎭 Conversão DTOs ↔ Entidades
│   │   │   ├── AutomovelFacade.java
│   │   │   ├── BancoFacade.java
│   │   │   ├── ClienteFacade.java
│   │   │   ├── ContratoCreditoFacade.java
│   │   │   ├── ContratoFacade.java
│   │   │   ├── EmpresaFacade.java
│   │   │   └── PedidoFacade.java
│   │   │
│   │   ├── service/                         # ⚙️ Lógica e Regras de Negócio
│   │   │   ├── AuthService.java
│   │   │   ├── AutomovelService.java
│   │   │   ├── BancoService.java
│   │   │   ├── ClienteService.java
│   │   │   ├── ContratoCreditoService.java
│   │   │   ├── ContratoService.java
│   │   │   ├── EmpresaService.java
│   │   │   └── PedidoService.java
│   │   │
│   │   ├── repository/                      # 🗄️ Acesso a Dados (JPA)
│   │   │   ├── AutomovelRepository.java
│   │   │   ├── BancoRepository.java
│   │   │   ├── ClienteRepository.java
│   │   │   ├── ContratoCreditoRepository.java
│   │   │   ├── ContratoRepository.java
│   │   │   ├── EmpresaRepository.java
│   │   │   └── PedidoRepository.java
│   │   │
│   │   ├── model/                           # 🧬 Entidades JPA
│   │   │   ├── Agente.java
│   │   │   ├── Automovel.java
│   │   │   ├── Banco.java
│   │   │   ├── Cliente.java
│   │   │   ├── Contrato.java
│   │   │   ├── ContratoCredito.java
│   │   │   ├── Empresa.java
│   │   │   ├── EntidadeEmpregadora.java
│   │   │   ├── Pedido.java
│   │   │   └── Usuario.java
│   │   │
│   │   ├── dto/                             # ✉️ Data Transfer Objects
│   │   │   ├── request/                     # DTOs de entrada (*RequestDTO)
│   │   │   └── response/                    # DTOs de saída (*ResponseDTO)
│   │   │
│   │   ├── exception/                       # 💥 Exceções e Handlers
│   │   │   ├── EntidadeNaoEncontradaException.java
│   │   │   ├── RegraDeNegocioException.java
│   │   │   └── GlobalExceptionHandler.java  # Tratamento global
│   │
│   └── resources/
│       ├── application.yml                  # ⚙️ Configuração principal
│       ├── swagger.yml                      # 📖 Contrato OpenAPI em YAML
│       ├── logback.xml                      # 📝 Configuração de logs
│       └── swagger-ui/
│           └── swagger-initializer.js       # 🎨 Customização Swagger UI
│
├── src/test/
│   ├── java/br/pucminas/                   # 🧪 Testes unitários/integração
│   └── resources/                          # ⚙️ Configurações de teste (opcional)
│
└── target/                                  # 📦 Artefatos compilados (JAR)
```

---

## 🧪 Testes

### Executar Testes Unitários

```powershell
.\mvnw.bat test
```

### Executar com Coverage

```powershell
.\mvnw.bat test jacoco:report
```

---

## 🔗 Documentações utilizadas

- [**Micronaut Documentation**](https://docs.micronaut.io/) — Framework e features principais
- [**Micronaut Data JPA**](https://micronaut-projects.github.io/micronaut-data/latest/guide/) — Acesso a dados e queries
- [**Micronaut OpenAPI / Swagger**](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/) — Documentação automática
- [**JJWT (JSON Web Token)**](https://github.com/jwtk/jjwt) — Autenticação via JWT
- [**Spring Data JPA**](https://spring.io/projects/spring-data-jpa) — Conceitos de persistência aplicáveis
- [**Conventional Commits**](https://www.conventionalcommits.org/) — Padrão de mensagens

---

## 🤝 Contribuição

1. Faça um _fork_ do repositório.
2. Crie uma branch (`git checkout -b feature/minha-feature`).
3. Commit com mensagens claras ([Conventional Commits](https://www.conventionalcommits.org/)).
4. Abra um _Pull Request_ descrevendo as mudanças.

---

## 📄 Licença

Este projeto está licenciado sob a **MIT License** — veja o arquivo [LICENSE](../LICENSE).

---

**Desenvolvido com ❤️ — [Voltar ao README Principal](../README.md)**