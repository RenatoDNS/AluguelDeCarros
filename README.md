# 🚗 Aluguel de Carros — API REST

> Backend em **Micronaut** para cadastro e gestão de **clientes** de um sistema de aluguel de veículos, com autenticação **JWT**, persistência **JPA** e documentação **OpenAPI/Swagger**.

<table>
  <tr>
    <td width="800px">
      <div align="justify">
        Este <b>README.md</b> descreve o projeto <b>Aluguel de Carros</b>, uma API REST que expõe operações de autenticação e CRUD de clientes (com entidades empregadoras), seguindo uma estrutura em camadas (controller, facade, service, repository). A documentação interativa fica disponível via <b>Swagger UI</b>; o contrato está em <code>src/main/resources/swagger.yml</code>.
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
- [Instalação e Execução](#-instalação-e-execução)
  - [Pré-requisitos](#pré-requisitos)
  - [Banco de Dados (H2)](#-banco-de-dados-h2)
  - [Como Executar a Aplicação](#-como-executar-a-aplicação)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [Documentações utilizadas](#-documentações-utilizadas)
- [Autores](#-autores)
- [Contribuição](#-contribuição)
- [Agradecimentos](#-agradecimentos)
- [Licença](#-licença)

---

## 🔗 Links Úteis

| Recurso | URL / caminho |
|--------|----------------|
| **OpenAPI (YAML)** | [`src/main/resources/swagger.yml`](backend/src/main/resources/swagger.yml) |
| **Diagramas (UML)** | [`docs/`](./docs/) — casos de uso e classes/pacotes (`.svg` / `.txt`) |

---

## 📝 Sobre o Projeto

O **Aluguel de Carros** é uma API de apoio a um cenário de **locação de veículos**, em que o cadastro de **clientes** é central: dados pessoais, senha com hash (**BCrypt**) e até **três entidades empregadoras** por cliente (validação de negócio e persistência).

- **Por que existe:** oferecer um serviço HTTP bem definido para registrar e consultar clientes, com login por CPF ou login cadastrado e proteção das rotas sensíveis com **JWT**.
- **Problema que endereça:** padronizar integrações (front-end ou outros sistemas) via REST e documentação OpenAPI.
- **Contexto:** projeto acadêmico / laboratorial na linha da Engenharia de Software (**PUC Minas**), com foco em boas práticas de camadas e documentação.

---

## ✨ Funcionalidades Principais

- **Cadastro público de cliente** — `POST /clientes` (sem token); inclui lista de entidades empregadoras (1 a 3 itens).
- **Autenticação JWT** — `POST /auth/login` retorna token e tempo de expiração; `POST /auth/logout` valida o token.
- **CRUD autenticado** — busca, atualização e exclusão de cliente por `id` com header `Authorization: Bearer <token>`.
- **Tratamento de erros** — respostas JSON com `mensagem` para não encontrado (404) e regra de negócio (422), além de 401 quando o token é inválido ou ausente nas rotas protegidas.
- **Documentação** — Swagger UI apontando para o arquivo OpenAPI em YAML.

---

## 🛠 Tecnologias Utilizadas

### 🖥️ Back-end

| Tecnologia | Uso |
|------------|-----|
| **Java 21** | Linguagem |
| **Micronaut 4.10** | Framework HTTP, injeção de dependências, validação |
| **Maven** | Build e dependências (`pom.xml`) |
| **H2** | Banco em memória (desenvolvimento); JDBC + Hibernate |
| **JPA / Hibernate** | Mapeamento objeto-relacional (`ddl-auto: update`) |
| **JJWT** | Geração e validação de tokens JWT |
| **BCrypt** (favre) | Hash de senhas |
| **Lombok** | Redução de boilerplate em parte do código |
| **micronaut-openapi** | Swagger UI |


---

## 🏗 Arquitetura

A aplicação segue um **monólito em camadas**:

1. **Controllers** (`controller`) — endpoints REST (`/auth`, `/clientes`).
2. **Facade** (`facade`) — orquestra conversão entre DTOs e entidades para clientes.
3. **Services** (`service`) — regras de autenticação e persistência de clientes.
4. **Repositories** (`repository`) — acesso a dados (Micronaut Data JPA).
5. **Config** — `JwtConfig`, `SecurityConfig` (rotas públicas), `JwtAuthenticationFilter` (filtro global de Bearer JWT).

**Padrões:** DTOs de entrada/saída, exceções de domínio com handler global, repositório para isolamento de persistência.

Diagramas de apoio (casos de uso e classes/pacotes) estão em [`docs/`](./docs/).

---

## 🔧 Instalação e Execução

### Pré-requisitos

- **JDK 21** (conforme `pom.xml`)
- **Maven** — o repositório inclui **Maven Wrapper** (`mvnw` / `mvnw.bat`); não é obrigatório ter Maven instalado globalmente.

---

### 💾 Banco de Dados (H2)

O perfil padrão usa **H2 em memória**: o schema é criado/atualizado pelo Hibernate na subida da aplicação. Não é necessário subir PostgreSQL nem Docker para desenvolvimento local básico.

Para persistir em arquivo, há exemplo comentado no `application.yml` (`jdbc:h2:file:./data/...`).

---

### ⚡ Como Executar a Aplicação

Na raiz do projeto:

**Windows**

```powershell
.\mvnw.bat mn:run
```

**Linux / macOS**

```bash
./mvnw mn:run
```

A API ficará em **http://localhost:8080** com context path **/api/aluguelcarros/v1** (ex.: `http://localhost:8080/api/aluguelcarros/v1/auth/login`).

---

## 📂 Estrutura de Pastas

Visão simplificada do repositório:

```
.
├── LICENSE
├── README.md
├── README_template.md          # template de referência (documentação)
├── pom.xml
├── mvnw / mvnw.bat          # Maven Wrapper (Unix / Windows)
├── micronaut-cli.yml
├── docs/                       # diagramas UML (SVG/TXT)
└── src/
    ├── main/
    │   ├── java/br/pucminas/aluguelcarros/
    │   │   ├── Application.java
    │   │   ├── config/         # JWT, segurança, filtro HTTP
    │   │   ├── controller/     # REST
    │   │   ├── dto/
    │   │   ├── exception/
    │   │   ├── facade/
    │   │   ├── model/
    │   │   ├── repository/
    │   │   └── service/
    │   └── resources/
    │       ├── application.yml
    │       ├── logback.xml
    │       └── swagger.yml       # OpenAPI
    └── test/java/              # testes JUnit / Micronaut Test
```

---

## 🔗 Documentações utilizadas

- [Micronaut Documentation](https://docs.micronaut.io/)
- [Micronaut Data JPA](https://micronaut-projects.github.io/micronaut-data/latest/guide/)
- [Micronaut OpenAPI / Swagger](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/)
- [JJWT](https://github.com/jwtk/jjwt)
- [Conventional Commits](https://www.conventionalcommits.org/)

---

## 👥 Autores

| Nome | GitHub | LinkedIn                                                                                                                | Licença / ano |
|------|--------|-------------------------------------------------------------------------------------------------------------------------|----------------|
| **Renato Douglas** | [![GitHub](https://img.shields.io/badge/GitHub-181717?logo=github)](https://github.com/RenatoDNS) | [![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?logo=linkedin)](https://www.linkedin.com/in/renatodns/) | Copyright © 2026 — ver [LICENSE](./LICENSE) |

---

## 🤝 Contribuição

1. Faça um *fork* do repositório.
2. Crie uma branch (`git checkout -b feature/minha-feature`).
3. Commit com mensagens claras (de preferência [Conventional Commits](https://www.conventionalcommits.org/)).
4. Abra um *Pull Request* descrevendo o que mudou.

---

## 🙏 Agradecimentos

- [**Engenharia de Software PUC Minas**](https://www.instagram.com/engsoftwarepucminas/) — apoio acadêmico e formação em engenharia de software.
- [**Prof. Dr. João Paulo Aramuni**](https://github.com/joaopauloaramuni) — referência em documentação e organização de projetos (incluindo o *template* de README que orientou esta documentação).

---

## 📄 Licença

Este projeto está licenciado sob a **MIT License** — veja o arquivo [LICENSE](./LICENSE).
