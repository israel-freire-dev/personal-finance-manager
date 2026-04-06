---
name: clean-architecture
description: >
  Guia Claude para desenvolver software seguindo os princípios da Clean Architecture (Arquitetura Limpa de Robert C. Martin).
  Use esta skill SEMPRE que o usuário pedir para criar, revisar, refatorar ou discutir qualquer código ou software —
  mesmo que não mencione explicitamente "Clean Architecture". Isso inclui pedidos como "crie uma API", "implemente esse
  recurso", "refatore esse código", "como estruturo esse projeto", ou qualquer outra demanda de desenvolvimento.
  Antes de começar, pergunte ao usuário se deseja usar os padrões da Clean Architecture no desenvolvimento.
  Cobre as quatro camadas: Entities, Use Cases, Interface Adapters e Frameworks & Drivers.
  Suporta Java/Kotlin (Spring Boot) e TypeScript/Node.js (Express, NestJS, Fastify).
---

# Clean Architecture Skill

## Visão Geral

Esta skill orienta Claude a **gerar e revisar código** seguindo os princípios da [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) de Robert C. Martin, garantindo software desacoplado, testável e fácil de manter.

---

## Fluxo de Interação Obrigatório

**Antes de qualquer geração de código**, siga este fluxo:

1. **Pergunte** ao usuário se deseja usar Clean Architecture:
   > "Gostaria de desenvolver isso seguindo os padrões da Clean Architecture (código desacoplado, fácil de testar e manter)? Se sim, me informe também a linguagem: Java/Kotlin ou TypeScript/Node.js."

2. **Se sim**, identifique:
   - Linguagem/framework alvo
   - Contexto do domínio (o que o software faz)
   - Quais camadas precisam ser criadas ou modificadas

3. **Se não**, desenvolva normalmente sem aplicar os padrões desta skill.

---

## As Quatro Camadas

```
╔══════════════════════════════════════╗
║   Frameworks & Drivers (Infra)       ║  ← Camada mais externa
║   ┌──────────────────────────────┐   ║
║   │  Interface Adapters           │   ║
║   │  ┌────────────────────────┐  │   ║
║   │  │  Use Cases (App)        │  │   ║
║   │  │  ┌──────────────────┐  │  │   ║
║   │  │  │  Entities (Domain)│  │  │   ║  ← Camada mais interna
║   │  │  └──────────────────┘  │  │   ║
║   │  └────────────────────────┘  │   ║
║   └──────────────────────────────┘   ║
╚══════════════════════════════════════╝
```

### Regra de Dependência (NUNCA violar!)
> Dependências sempre apontam **para dentro**. Camadas internas **nunca** conhecem camadas externas.

---

## Estrutura de Pastas por Linguagem

### TypeScript / Node.js

```
src/
├── domain/                        # Entities
│   ├── entities/
│   │   └── User.ts
│   └── errors/
│       └── DomainError.ts
│
├── application/                   # Use Cases
│   ├── use-cases/
│   │   └── CreateUserUseCase.ts
│   ├── ports/                     # Interfaces (contratos)
│   │   ├── repositories/
│   │   │   └── IUserRepository.ts
│   │   └── services/
│   │       └── IHashService.ts
│   └── dtos/
│       └── CreateUserDTO.ts
│
├── adapters/                      # Interface Adapters
│   ├── controllers/
│   │   └── UserController.ts
│   ├── presenters/
│   │   └── UserPresenter.ts
│   └── repositories/              # Implementações concretas dos ports
│       └── PrismaUserRepository.ts
│
└── infra/                         # Frameworks & Drivers
    ├── database/
    │   └── prisma/
    ├── http/
    │   ├── routes/
    │   └── middlewares/
    └── container/                 # Injeção de dependência
        └── index.ts
```

### Java / Kotlin (Spring Boot)

```
src/main/
├── domain/
│   ├── entity/
│   │   └── User.kt
│   ├── exception/
│   │   └── DomainException.kt
│   └── valueobject/
│       └── Email.kt
│
├── application/
│   ├── usecase/
│   │   └── CreateUserUseCase.kt
│   ├── port/
│   │   ├── input/
│   │   │   └── CreateUserInputPort.kt
│   │   └── output/
│   │       └── UserRepositoryPort.kt
│   └── dto/
│       └── CreateUserCommand.kt
│
├── adapter/
│   ├── inbound/
│   │   ├── web/
│   │   │   └── UserController.kt
│   │   └── messaging/
│   └── outbound/
│       ├── persistence/
│       │   └── UserJpaRepository.kt
│       └── service/
│
└── infrastructure/
    ├── config/
    │   └── BeanConfig.kt
    └── persistence/
        └── entity/
            └── UserJpaEntity.kt
```

---

## Padrões por Camada

### 1. Entities (Domínio)
- Contém regras de negócio **puras**, sem dependência de frameworks
- Use **Value Objects** para conceitos com regras (ex: `Email`, `CPF`, `Money`)
- Entidades **nunca** acessam banco de dados ou HTTP
- Erros de domínio são lançados dentro das entidades

**Boas práticas:**
- Validação no construtor
- Imutabilidade onde possível
- Métodos de negócio expressivos (`user.activate()`, `order.cancel()`)

### 2. Use Cases (Aplicação)
- Orquestram entidades para cumprir **um único objetivo**
- Dependem apenas de **interfaces (ports)**, nunca de implementações concretas
- Seguem o princípio de **Single Responsibility**
- Recebem e retornam **DTOs**, nunca entidades de infraestrutura

**Boas práticas:**
- Um arquivo por caso de uso
- Nome descritivo: `CreateUserUseCase`, `ApproveOrderUseCase`
- Tratar erros de domínio e lançar exceções de aplicação quando necessário

### 3. Interface Adapters
- Convertem dados entre o formato externo (HTTP, mensageria) e o formato interno (DTOs/Entities)
- **Controllers**: recebem requests, chamam use cases, retornam responses
- **Presenters**: formatam saída para o cliente
- **Repository Implementations**: implementam os ports definidos na camada de aplicação

**Boas práticas:**
- Controllers devem ser "burros" — apenas delegam ao use case
- Nunca colocar regra de negócio aqui
- Mapear exceções de domínio para status HTTP corretos

### 4. Frameworks & Drivers (Infra)
- Configuração de banco de dados, HTTP server, filas, etc.
- Injeção de dependência (IoC Container)
- Implementações de bibliotecas externas

**Boas práticas:**
- Isolar configurações de framework aqui
- Usar variáveis de ambiente para configurações sensíveis
- Registrar todas as dependências no container

---

## Geração de Código

Ao gerar código, sempre:

1. **Gere todas as camadas necessárias** para o feature solicitado, não apenas uma parte
2. **Mostre a estrutura de pastas** antes do código
3. **Nomeie arquivos e classes** de forma expressiva e consistente
4. **Adicione comentários** explicando o papel de cada classe/interface
5. **Inclua os imports** necessários
6. **Indique** onde cada arquivo deve ser criado

### Checklist de Revisão (use ao revisar código existente)

- [ ] As entidades têm dependências externas? (❌ não devem ter)
- [ ] Os use cases dependem de implementações concretas? (❌ não devem)
- [ ] Há regra de negócio no controller? (❌ não deve ter)
- [ ] Os ports (interfaces) estão na camada de aplicação? (✅ devem estar)
- [ ] As implementações de repository estão nos adapters? (✅ devem estar)
- [ ] A injeção de dependência está na infra? (✅ deve estar)
- [ ] Os DTOs são usados para transferência entre camadas? (✅ devem ser)

---

## Exemplos de Referência

Para exemplos completos de código, consulte os arquivos em `references/`:
- `references/typescript-example.md` — Exemplo completo em TypeScript/Node.js
- `references/kotlin-example.md` — Exemplo completo em Kotlin/Spring Boot

---

## Princípios SOLID Aplicados

| Princípio | Aplicação na Clean Architecture |
|-----------|--------------------------------|
| **S** — Single Responsibility | Um use case por arquivo |
| **O** — Open/Closed | Ports (interfaces) permitem extensão sem modificação |
| **L** — Liskov Substitution | Implementações de ports são intercambiáveis |
| **I** — Interface Segregation | Ports específicos por operação |
| **D** — Dependency Inversion | Use cases dependem de abstrações, não implementações |
