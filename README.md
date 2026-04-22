# 🚗 Aluguel de Carros — Sistema Completo

> Sistema **full-stack** de aluguel de carros com API REST em **Micronaut** (Backend) e interface web em **Angular** (Frontend), incluindo autenticação **JWT**, persistência em **PostgreSQL/JPA** e documentação **OpenAPI/Swagger**.

<table>
  <tr>
    <td width="800px">
      <div align="justify">
        Este <b>README.md</b> descreve o projeto completo <b>Aluguel de Carros</b>, uma aplicação full-stack para gestão de um sistema de locação e compra a crédito de veículos. Composto por dois principais componentes: <b>Backend</b> (Micronaut com API REST documentada) e <b>Frontend</b> (Angular com interface responsiva por perfil). O projeto segue boas práticas de engenharia de software e está organizado como um monorepo com documentação, testes e arquitetura bem definidos.
      </div>
    </td>
    <td>
      <div align="center">🚙</div>
    </td>
  </tr>
</table>

---

## 🚧 Status do Projeto

[![Versão](https://img.shields.io/badge/versão-0.1-blue)](backend/pom.xml)
[![Java](https://img.shields.io/badge/Java-21-437291?logo=openjdk&logoColor=white)](backend/pom.xml)
[![Micronaut](https://img.shields.io/badge/Micronaut-4.10-007ec6)](backend/pom.xml)
[![Licença](https://img.shields.io/badge/licença-MIT-green)](./LICENSE)

---

## 📚 Índice

- [Links Úteis](#-links-úteis)
- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [Instalação e Execução](#-instalação-e-execução)
  - [Pré-requisitos](#pré-requisitos)
  - [Banco de Dados (PostgreSQL)](#-banco-de-dados-postgresql)
  - [Instalação de Dependências](#-instalação-de-dependências)
  - [Como Executar a Aplicação](#-como-executar-a-aplicação)
- [Documentações dos Componentes](#-documentações-dos-componentes)
- [Documentações Utilizadas](#-documentações-utilizadas)
- [Autores](#-autores)
- [Contribuição](#-contribuição)
- [Agradecimentos](#-agradecimentos)
- [Licença](#-licença)

---

## 🔗 Links Úteis

| Recurso                  | URL / Caminho                                                                      |
| ------------------------ | ---------------------------------------------------------------------------------- |
| **📖 Backend (README)**  | [`backend/README.md`](backend/README.md)                                           |
| **🌐 Frontend (README)** | [`frontend/README.md`](frontend/README.md)                                         |
| **📊 OpenAPI (YAML)**    | [`backend/src/main/resources/swagger.yml`](backend/src/main/resources/swagger.yml) |
| **🎨 Swagger UI**        | `http://localhost:8080/api/aluguelcarros/v1/swagger-ui` (ao executar backend)      |
| **📐 Diagramas UML**     | [`docs/`](./docs/) — casos de uso e classes/pacotes (`.svg` / `.txt`)              |

---

## 📝 Sobre o Projeto

O **Aluguel de Carros** é uma aplicação full-stack de **locação e compra a crédito de veículos**, com três perfis distintos de usuário: **cliente**, **empresa** e **banco**.

- **Por que existe:** oferecer um sistema HTTP bem definido e acessível para registrar, consultar e gerenciar o ciclo completo de pedidos — da solicitação à assinatura do contrato — com autenticação segura (**JWT**) e interface amigável.
- **Problema que resolve:** padroniza integrações via REST com documentação OpenAPI; oferece UI responsiva diferenciada por perfil.
- **Contexto:** projeto acadêmico / laboratorial na linha da Engenharia de Software (**PUC Minas**), com foco em boas práticas de camadas, documentação e arquitetura moderna.

---

## ✨ Funcionalidades Principais

- **Cadastro público** — `POST /clientes`, `POST /empresas`, `POST /bancos` (sem token).
- **Autenticação JWT** — `POST /auth/login` retorna token com `userType`; `GET /auth/me` retorna o perfil do token.
- **Gestão de automóveis** — cadastro, atualização, remoção e listagem (global, por agente e por status).
- **Fluxo de pedidos** — cliente cria pedido de aluguel ou compra a crédito; agente (empresa/banco) avalia com aprovação ou rejeição.
- **Geração automática de contratos** — ao aprovar um pedido, o contrato correspondente (aluguel ou crédito) é criado automaticamente.
- **Assinatura de contratos** — empresa/banco e cliente assinam separadamente via `POST /contratos/{id}/assinar` ou `POST /contratos-credito/{id}/assinar`.
- **Tratamento de erros** — respostas JSON com `mensagem` para 404, 422 e 401.
- **Documentação interativa** — Swagger UI apontando para o contrato OpenAPI em YAML.
- **Interface web responsiva** — dashboard diferenciado por perfil com Angular.

---

## 🛠 Tecnologias Utilizadas

### 🖥️ Back-end

| Tecnologia            | Uso                                                  |
| --------------------- | ---------------------------------------------------- |
| **Java 21**           | Linguagem                                            |
| **Micronaut 4.10**    | Framework HTTP, injeção de dependências, validação   |
| **Maven**             | Build e dependências (`pom.xml`)                     |
| **PostgreSQL 16+**    | Banco relacional padrão; H2 em memória para testes   |
| **JPA / Hibernate**   | Mapeamento objeto-relacional (`ddl-auto: update`)    |
| **JJWT**              | Geração e validação de tokens JWT                    |
| **BCrypt** (favre)    | Hash de senhas                                       |
| **Lombok**            | Redução de boilerplate em parte do código            |
| **micronaut-openapi** | Swagger UI                                           |

### 💻 Front-end

| Tecnologia           | Uso                                 |
| -------------------- | ----------------------------------- |
| **Angular 21.2.6**   | Framework web e componentes         |
| **TypeScript 5.x**   | Linguagem tipada                    |
| **RxJS 7.x**         | Programação reativa com observables |
| **HttpClient**       | Requisições HTTP para a API         |
| **Angular Router**   | Roteamento com lazy loading e guards|
| **Angular Forms**    | Formulários reativos                |
| **Node.js 20.x LTS** | Runtime para desenvolvimento        |
| **npm**              | Gerenciador de dependências         |

## 🏗 Arquitetura

A aplicação segue um **monólito em camadas** no backend e uma **arquitetura em componentes** no frontend:

**Backend:**

1. **Controllers** (`controller`) — endpoints REST (`/auth`, `/clientes`, `/bancos`, `/empresas`, `/automoveis`, `/pedidos`, `/agente/pedidos`, `/contratos`, `/contratos-credito`).
2. **Facade** (`facade`) — orquestra conversão entre DTOs e entidades.
3. **Services** (`service`) — regras de autenticação e lógica de negócio.
4. **Repositories** (`repository`) — acesso a dados (Micronaut Data JPA).
5. **Config** — `JwtConfig`, `SecurityConfig`, `JwtAuthenticationFilter`.

**Frontend:**

1. **Pages** — componentes de página com lazy loading, por perfil.
2. **Services** — `AuthService`, `VeiculoService`, `PedidoService`, `ContratoService`.
3. **Guards** — `authGuard` (autenticação) e `roleGuard` (perfil).

**Padrões:** DTOs de entrada/saída, exceções de domínio com handler global, repositório para isolamento de persistência.

Diagramas de apoio (casos de uso e classes/pacotes) estão em [`docs/`](./docs/).

## 🔧 Instalação e Execução

### Pré-requisitos

**Para rodar o backend via Docker (recomendado):**
- **Docker** e **Docker Compose**
- **Node.js 20.x LTS** (para o frontend)

**Para rodar tudo localmente (sem Docker):**
- **JDK 21**
- **Node.js 20.x LTS**
- **PostgreSQL 16+**
- **Maven** — o repositório inclui **Maven Wrapper** (`mvnw` / `mvnw.bat`); não é obrigatório ter Maven instalado globalmente.

---

### 💾 Banco de Dados (PostgreSQL)

O backend utiliza **PostgreSQL**. A maneira mais rápida de subir o banco é via Docker — e com o novo `docker-compose.yml`, a própria API já sobe junto. Consulte `backend/README.md` para detalhes completos de configuração e variáveis de ambiente.

---

### 📦 Instalação de Dependências

1. **Clone o Repositório:**

```bash
git clone https://github.com/RenatoDNS/AluguelDeCarros.git
cd AluguelDeCarros
```

2. **Instale as Dependências do Frontend:**

```bash
cd frontend
npm install
cd ..
```

---

### ⚡ Como Executar a Aplicação

#### 🐳 Backend via Docker (recomendado)

Sobe a API e o PostgreSQL juntos, sem instalar Java localmente:

```bash
cd backend
docker compose up -d --build
```

✅ **A API ficará disponível em:** `http://localhost:8080/api/aluguelcarros/v1`
✅ **Swagger UI disponível em:** `http://localhost:8080/api/aluguelcarros/v1/swagger-ui`

---

#### 💻 Backend local (sem Docker)

Requer JDK 21 e PostgreSQL rodando. Exporte as variáveis do `.env.example` e execute:

**Windows:**

```powershell
cd backend
.\mvnw.bat mn:run
```

**Linux / macOS:**

```bash
cd backend
./mvnw mn:run
```

✅ **A API ficará disponível em:** `http://localhost:8080/api/aluguelcarros/v1`

---

#### 🌐 Frontend (em qualquer caso)

```bash
cd frontend
npm start
```

✅ **O frontend estará disponível em:** `http://localhost:4200`

---

## 📂 Estrutura de Pastas

Visão simplificada do repositório:

```
.
├── LICENSE
├── README.md
│
├── /backend                    # 📁 Aplicação Micronaut
│   ├── README.md               # 📖 Documentação específica do Backend
│   ├── pom.xml
│   ├── mvnw / mvnw.bat         # Maven Wrapper (Unix / Windows)
│   └── src/
│       ├── main/
│       │   ├── java/br/pucminas/aluguelcarros/
│       │   │   ├── Application.java
│       │   │   ├── config/         # JWT, segurança, filtro HTTP
│       │   │   ├── controller/     # REST
│       │   │   ├── dto/
│       │   │   ├── exception/
│       │   │   ├── facade/
│       │   │   ├── model/
│       │   │   ├── repository/
│       │   │   └── service/
│       │   └── resources/
│       │       ├── application.yml
│       │       ├── logback.xml
│       │       └── swagger.yml       # OpenAPI
│       └── test/java/              # testes JUnit / Micronaut Test
│
├── /frontend                   # 📁 Aplicação Angular
│   ├── README.md               # 📖 Documentação específica do Frontend
│   ├── package.json
│   ├── angular.json
│   └── src/
│       ├── main.ts
│       ├── index.html
│       ├── styles.css
│       └── app/
│           ├── app.ts
│           ├── app.routes.ts
│           ├── guards/
│           ├── interceptors/
│           ├── models/
│           ├── pages/
│           ├── services/
│           └── components/
│
└── /docs                       # 📚 Documentação e Diagramas
    ├── DiagramaDeCasosDeUso.svg
    ├── DiagramaDeClassesEPacotes.svg
    └── HistoriasDeUsuario.pdf
```

---

## 📚 Documentações dos Componentes

- **[📖 Backend README](backend/README.md)** — Documentação técnica completa do Backend Micronaut
- **[🌐 Frontend README](frontend/README.md)** — Documentação técnica completa do Frontend Angular

---

## 🔗 Documentações Utilizadas

- [**João Paulo Aramuni - GitHub**](https://github.com/joaopauloaramuni) — Templates de documentações
- [Micronaut Documentation](https://docs.micronaut.io/)
- [Micronaut Data JPA](https://micronaut-projects.github.io/micronaut-data/latest/guide/)
- [Micronaut OpenAPI / Swagger](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/)
- [JJWT](https://github.com/jwtk/jjwt)
- [Angular Documentation](https://angular.dev/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Conventional Commits](https://www.conventionalcommits.org/)

---

## 👥 Autores

| Nome                | GitHub                                                                                             | LinkedIn                                                                                                                | Licença / ano                               |
| ------------------- | -------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------- | ------------------------------------------- |
| **Renato Douglas**  | [![GitHub](https://img.shields.io/badge/GitHub-181717?logo=github)](https://github.com/RenatoDNS)  | [![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?logo=linkedin)](https://www.linkedin.com/in/renatodns/)       | Copyright © 2026 — ver [LICENSE](./LICENSE) |
| **Vicenzo Fonseca** | [![GitHub](https://img.shields.io/badge/GitHub-181717?logo=github)](https://github.com/VicenzoFMS) | [![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?logo=linkedin)](https://www.linkedin.com/in/vicenzo-fonseca/) | Copyright © 2026 — ver [LICENSE](./LICENSE) |

---

## 🤝 Contribuição

1. Faça um _fork_ do repositório.
2. Crie uma branch (`git checkout -b feature/minha-feature`).
3. Commit com mensagens claras (de preferência [Conventional Commits](https://www.conventionalcommits.org/)).
4. Abra um _Pull Request_ descrevendo o que mudou.

---

## 🙏 Agradecimentos

- [**Engenharia de Software PUC Minas**](https://www.instagram.com/engsoftwarepucminas/) — apoio acadêmico e formação em engenharia de software.
- [**Prof. Dr. João Paulo Aramuni**](https://github.com/joaopauloaramuni) — referência em documentação e organização de projetos (incluindo o _template_ de README que orientou esta documentação).

---

## 📄 Licença

Este projeto está licenciado sob a **MIT License** — veja o arquivo [LICENSE](./LICENSE).