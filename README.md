# Sistema de Aluguel de Carros

Sistema web para gestão de aluguéis de automóveis, desenvolvido em Java com Micronaut (MVC). Permite que clientes gerenciem pedidos de aluguel e que agentes realizem análise de crédito.

## Stack Técnica

| Camada | Tecnologia |
|--------|-----------|
| Linguagem | Java 17 |
| Framework | Micronaut 4.7.x |
| Servidor | Netty (embutido) |
| Templates | Thymeleaf |
| Persistência | Hibernate JPA + HikariCP |
| Banco de dados | H2 (in-memory) |
| Build | Maven 3.9+ |
| Container | Docker |

## Arquitetura

O sistema segue arquitetura **MVC em camadas**, mapeada ao diagrama de pacotes da Sprint 1:

```
presentation   → Controllers HTTP + DTOs + Templates Thymeleaf
application    → Serviços de casos de uso (regras de negócio)
interfaces     → Contratos dos repositórios (interfaces puras)
infrastructure → Implementações dos repositórios (EntityManager/JPA)
domain         → Entidades, enums e value objects (modelo de domínio)
```

Consulte [DIAGRAMAS.md](DIAGRAMAS.md) para os diagramas UML completos.

## Pré-requisitos

- **Docker** (recomendado) — sem necessidade de instalar Java ou Maven localmente
- **OU**: Java 17+ e Maven 3.9+ para execução local

## Como Executar

### Via Docker (recomendado)

```bash
docker compose up --build
```

Acesse em: [http://localhost:8080/clientes/](http://localhost:8080/clientes/)

Para encerrar:

```bash
docker compose down
```

### Via Maven (desenvolvimento local)

```bash
mvn mn:run
```

Acesse em: [http://localhost:8080/clientes/](http://localhost:8080/clientes/)

## Estrutura do Projeto

```
src/main/java/com/lab02/aluguelcarros/
├── Application.java                   # Entry point Micronaut
├── domain/
│   ├── shared/                        # Value objects: CPF, Money + validação
│   ├── cliente/                       # Entidades: Cliente, Rendimento
│   └── pedido/                        # Enum: StatusPedido
├── interfaces/
│   └── ClienteRepository.java         # Contrato do repositório
├── infrastructure/
│   └── ClienteRepositoryImpl.java     # Implementação JPA com EntityManager
├── application/
│   └── ClienteService.java            # Casos de uso do CRUD de cliente
└── presentation/
    ├── ClienteController.java          # Controller MVC (@Controller)
    └── dto/
        ├── ClienteForm.java            # DTO para binding de formulário
        └── RendimentoForm.java

src/main/resources/
├── application.yml                    # Configuração Micronaut + H2 + JPA
└── views/
    ├── layout.html                    # Layout base (navbar + Bootstrap 5)
    └── cliente/
        ├── list.html                  # Listagem de clientes
        ├── form.html                  # Formulário de criação/edição
        └── detail.html               # Detalhes do cliente
```

## Funcionalidades Implementadas (Sprint 2)

- **Listar clientes** — tabela com nome, CPF, RG, profissão e número de rendimentos
- **Cadastrar cliente** — formulário com validação de CPF (dígitos verificadores), campos obrigatórios e até 3 rendimentos
- **Visualizar cliente** — página de detalhes com todos os dados e rendimentos
- **Editar cliente** — mesmo formulário com dados pré-preenchidos
- **Excluir cliente** — remoção com confirmação via dialog nativo
- **Validações**: CPF com algoritmo de dígitos verificadores, unicidade de CPF no banco, campos obrigatórios, máximo 3 rendimentos

## Documentação

- [HISTORIAS.md](HISTORIAS.md) — Histórias de usuário (Cliente e Agente)
- [DIAGRAMAS.md](DIAGRAMAS.md) — Diagramas UML (Casos de Uso, Pacotes, Classes)
