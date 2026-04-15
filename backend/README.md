# 🚗 Aluguel de Carros — API REST (Backend)

> API REST em **Micronaut** para autenticação, CRUD completo das entidades de domínio e fluxos de negócio, com integração ao banco **H2** e documentação **OpenAPI/Swagger**.

<table>
  <tr>
    <td width="800px">
      <div align="justify">
        Este <b>README.md</b> descreve o <b>Backend</b> do projeto <b>Aluguel de Carros</b>, uma API REST que implementa autenticação via JWT, CRUD completo de clientes, empresas, bancos, automóveis, pedidos, pareceres, contratos e contratos de crédito, além de fluxos de negócio por perfil. Segue uma arquitetura em camadas (controller, facade, service, repository) com persistência via JPA/Hibernate. A documentação interativa fica disponível via <b>Swagger UI</b> em <code>src/main/resources/swagger.yml</code>.
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
  - [Banco de Dados (H2)](#-banco-de-dados-h2)
  - [Como Executar a Aplicação](#-como-executar-a-aplicação)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [Testes](#-testes)
- [Documentações utilizadas](#-documentações-utilizadas)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

---

## 🔗 Links Úteis

| Recurso | Caminho / URL |
|---------|---------------|
| **OpenAPI (YAML)** | [`src/main/resources/swagger.yml`](src/main/resources/swagger.yml) |
| **Swagger UI** | `http://localhost:8080/api/aluguelcarros/v1/swagger-ui` |
| **Documentação Técnica** | [`../docs/`](../docs/) — diagramas UML (casos de uso e classes/pacotes) |

---

## 📝 Sobre o Projeto

O **Backend do Aluguel de Carros** é uma API HTTP desenvolvida com **Micronaut** que oferece:

- **Gestão das entidades de domínio** — clientes, empresas, bancos, automóveis, pedidos, pareceres, contratos e contratos de crédito.
- **Autenticação JWT** — emissão e validação de tokens para proteção de rotas sensíveis.
- **API REST bem definida** — endpoints documentados via OpenAPI/Swagger com tratamento padronizado de erros.
- **Persistência com JPA/Hibernate** — usando banco H2 em arquivo local por padrão (`./data/aluguelcarros`).

**Por que existe:** oferece um serviço HTTP seguro e bem documentado para integração com front-ends e outros sistemas.

**Problema que resolve:** padroniza a comunicação entre cliente e servidor via REST, com autenticação robusta e documentação automática.

**Contexto:** projeto acadêmico na PUC Minas focado em boas práticas de engenharia de software e arquitetura em camadas.

---

## ✨ Funcionalidades Principais

- 🔐 **Cadastro Público de Cliente** — `POST /clientes` (sem autenticação); inclui lista de 1 a 3 entidades empregadoras.
- 🔑 **Autenticação JWT por Perfil** — `POST /auth/login` por CPF (cliente) ou CNPJ (empresa/banco), com retorno de `userType`.
- 👤 **Perfil Autenticado** — `GET /auth/me` retorna `id`, `login` e `userType` a partir do token.
- 📋 **CRUDs completos** — `clientes`, `bancos`, `empresas`, `automoveis`, `pedidos`, `pareceres`, `contratos` e `contratos-credito`.
- 🔄 **Fluxos de Negócio por Perfil** — cancelamento de pedido, avaliação de pedido por agente, associação de crédito por banco, execução de contrato por pedido e listagens por contexto autenticado.
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

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| **Java** | 21 | Linguagem de programação |
| **Micronaut** | 4.10 | Framework HTTP, DI, validação |
| **Maven** | 3.9+ | Build e gerenciador de dependências |
| **H2 Database** | Latest | Banco de dados em memória (development) |
| **JPA / Hibernate** | Latest | Mapeamento objeto-relacional |
| **JJWT** | Latest | Geração e validação de tokens JWT |
| **BCrypt (favre)** | Latest | Hash seguro de senhas |
| **Lombok** | Latest | Redução de boilerplate |
| **micronaut-openapi** | Latest | Swagger UI e OpenAPI |

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

1. **Controllers** (`controller/`) — Endpoints REST (`/auth`, `/clientes`), validação de entrada.
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

- **JDK 21** — conforme especificado em `pom.xml`
- **Maven** (opcional) — o projeto inclui **Maven Wrapper** (`mvnw` / `mvnw.bat`)

---

### 💾 Banco de Dados (H2)

O projeto utiliza **H2 em arquivo** por padrão (`./data/aluguelcarros`): o schema é criado/atualizado automaticamente pelo Hibernate na inicialização.

Trecho atual em `src/main/resources/application.yml`:

```yaml
datasources:
  default:
    url: jdbc:h2:file:./data/aluguelcarros;DB_CLOSE_DELAY=-1;MODE=LEGACY
    username: sa
    password: ''
    driver-class-name: org.h2.Driver
```

---

### ⚡ Como Executar a Aplicação

**Windows:**

```powershell
.\mvnw.bat mn:run
```

**Linux / macOS:**

```bash
./mvnw mn:run
```

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
│   │   │   ├── BancoController.java / EmpresaController.java
│   │   │   ├── AutomovelController.java / PedidoController.java
│   │   │   ├── ParecerController.java / ContratoController.java
│   │   │   ├── ContratoCreditoController.java
│   │   │   ├── AgentePedidoController.java / BancoPedidoController.java
│   │   │   └── SwaggerController.java
│   │   │
│   │   ├── facade/                          # 🎭 Conversão DTOs ↔ Entidades
│   │   │   ├── *Facade.java (Cliente/Banco/Empresa/Automovel/...)
│   │   │   └── Facades de fluxo por entidade
│   │   │
│   │   ├── service/                         # ⚙️ Lógica e Regras de Negócio
│   │   │   ├── AuthService.java
│   │   │   ├── *Service.java (CRUD e regras de negócio)
│   │   │   └── Serviços de fluxo por perfil
│   │   │
│   │   ├── repository/                      # 🗄️ Acesso a Dados (JPA)
│   │   │   ├── ClienteRepository.java / EmpresaRepository.java / BancoRepository.java
│   │   │   ├── AutomovelRepository.java / PedidoRepository.java / ParecerRepository.java
│   │   │   └── ContratoRepository.java / ContratoCreditoRepository.java
│   │   │
│   │   ├── model/                           # 🧬 Entidades JPA
│   │   │   ├── Cliente.java / EntidadeEmpregadora.java / Automovel.java
│   │   │   ├── Pedido.java / Parecer.java / Contrato.java / ContratoCredito.java
│   │   │   └── Empresa.java / Banco.java / Usuario.java / Agente.java
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
│   └── resources/application-test.yml      # ⚙️ Configuração de teste (H2 em memória)
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

1. Faça um *fork* do repositório.
2. Crie uma branch (`git checkout -b feature/minha-feature`).
3. Commit com mensagens claras ([Conventional Commits](https://www.conventionalcommits.org/)).
4. Abra um *Pull Request* descrevendo as mudanças.

---

## 📄 Licença

Este projeto está licenciado sob a **MIT License** — veja o arquivo [LICENSE](../LICENSE).

---

**Desenvolvido com ❤️ — [Voltar ao README Principal](../README.md)**

