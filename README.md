# NGN – Nova Gaia Nexus

> **Sistema aeroespacial de monitoramento e gestão de habitats espaciais**  
> Projeto da disciplina de Engenharia de Software — Global Solution  

---

## Descrição do Problema

A humanidade estabeleceu estações espaciais e bases lunares para colonização.  
Esses habitats precisam de monitoramento contínuo e automatizado de parâmetros vitais — oxigênio, radiação, água, energia e produção agrícola — além de resposta autônoma a eventos espaciais perigosos como meteoros e tempestades solares.

## Objetivo da Solução

O **NGN** oferece uma API REST completa para:

- Monitorar habitats espaciais em tempo real via sensores
- Detectar eventos espaciais (NASA DONKI/NeoWs) e emitir alertas automáticos
- Acionar protocolos de defesa autônomos via inteligência artificial
- Controlar módulos, tripulação, energia e recursos hídricos
- Disponibilizar dashboards consolidados por habitat

---

## Decisões Arquiteturais

| Decisão | Justificativa |
|---------|---------------|
| **Arquitetura em camadas** | Controller → Service → Repository: baixo acoplamento, alta coesão |
| **Strategy Pattern** | Cada tipo de alerta tem uma estratégia de resposta independente e extensível |
| **Specification Pattern** | Filtros dinâmicos nos endpoints sem duplicar queries |
| **DTOs (Records)** | Entidades JPA nunca expostas; contratos imutáveis na API |
| **@Transactional granular** | `readOnly=true` nas leituras; rollback automático nas escritas |
| **Spring Security RBAC** | ADMIN, OPERATOR, VIEWER — parâmetros críticos protegidos (RN07) |
| **Spring Scheduling** | Monitoramento periódico de sensores e sincronização com NASA |

---

## Decisões Técnicas

- **Java 21** com Records, switch expressions e Text Blocks
- **Spring Boot 3.4.1** com auto-configuration
- **Spring Data JPA + Hibernate** com Specifications para queries dinâmicas
- **H2 in-memory** com `data.sql` para dados de demonstração
- **Lombok** para eliminação de boilerplate
- **SpringDoc/Swagger** em `/swagger-ui.html`
- **NASA DONKI API** — eventos solares; **NASA NeoWs** — asteroides próximos

---

## Estratégias de Modelagem

### Entidades Centrais (8)
| Entidade | Papel |
|----------|-------|
| `Habitat` | Estação espacial; agrega módulos, alertas e métricas |
| `Module` | Setor funcional (energia, defesa, agricultura…) |
| `Sensor` | Dispositivo de leitura em tempo real |
| `Alert` | Ocorrência de risco com nível de severidade |
| `AIAction` | Ação autônoma disparada em resposta a alertas |
| `CrewMember` | Tripulante alocado em módulos |
| `SpaceEvent` | Evento espacial externo (meteoro, tempestade solar) |
| `ResourceMetric` | Snapshot periódico de todos os recursos do habitat |

### Relacionamentos
- `Habitat` →|< `Module` (OneToMany)
- `Habitat` →|< `Alert` (OneToMany)
- `Habitat` →|< `ResourceMetric` (OneToMany)
- `Module` →|< `Sensor` (OneToMany)
- `Alert` →|< `AIAction` (OneToMany)
- `Module` ><< `CrewMember` (**ManyToMany** via `tb_module_crew`)
- `Habitat` usa `AtmosphericCondition` (**@Embedded**)
- `Sensor` e `SpaceEvent` usam `Coordinates` (**@Embedded**)

### Design Patterns
| Padrão | Onde | Propósito |
|--------|------|-----------|
| **Strategy** | `AIResponseStrategy` + 5 implementações | Resposta autônoma por tipo de alerta sem if/else |
| **Specification** | `AlertSpecification`, `ModuleSpecification`, `SpaceEventSpecification` | Filtros dinâmicos compostos na API |
| **Repository** | 8 interfaces Spring Data | Isolamento da persistência |
| **Mapper** | 8 classes `*Mapper` | Separação domínio ↔ API |

---

## Tecnologias Utilizadas

- Java 21 · Spring Boot 3.4.1 · Spring Data JPA · Spring Security
- Hibernate · H2 Database · Maven
- Lombok · SpringDoc OpenAPI 2.7 · RestTemplate (NASA APIs)

---

## Instruções de Execução

### Pré-requisitos
- Java 21+
- Maven 3.9+

### Executar
```bash
git clone <URL_DO_REPO>
cd ngn
mvn spring-boot:run
```

### Acessar
| Recurso | URL |
|---------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| H2 Console | http://localhost:8080/h2-console |
| API Docs   | http://localhost:8080/api-docs |

### Credenciais padrão (Basic Auth)
| Usuário | Senha | Role |
|---------|-------|------|
| `admin` | `admin123` | ADMIN |
| `operator` | `op123` | OPERATOR |
| `viewer` | `view123` | VIEWER |

### H2 Console
- JDBC URL: `jdbc:h2:mem:ngndb`
- User: `sa` · Password: *(vazio)*

---

## Endpoints Principais

### Habitats
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET`    | `/api/v1/habitats` | Listar (paginado) |
| `POST`   | `/api/v1/habitats` | Criar habitat |
| `GET`    | `/api/v1/habitats/{id}` | Buscar por ID |
| `PUT`    | `/api/v1/habitats/{id}` | Atualizar |
| `DELETE` | `/api/v1/habitats/{id}` | Excluir *(ADMIN)* |
| `PATCH`  | `/api/v1/habitats/{id}/atmosphere` | Atualizar condições atmosféricas *(ADMIN)* |

### Módulos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `PATCH` | `/api/v1/modules/{id}/activate` | Ativar módulo *(ADMIN)* |
| `PATCH` | `/api/v1/modules/{id}/deactivate` | Desativar *(ADMIN / não-essenciais)* |
| `POST`  | `/api/v1/modules/{id}/crew/{crewId}` | Alocar tripulante |
| `GET`   | `/api/v1/modules/habitat/{habitatId}/consumption` | Consumo energético total |

### Alertas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET`    | `/api/v1/alerts/filter` | Filtrar por tipo, nível, status |
| `PATCH`  | `/api/v1/alerts/{id}/resolve` | Resolver alerta |
| `POST`   | `/api/v1/alerts/{id}/trigger-response` | Disparar resposta automática *(ADMIN)* |

### Eventos Espaciais
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET`  | `/api/v1/space-events/high-danger` | Eventos acima do limiar (≥ 7.0) |
| `POST` | `/api/v1/space-events/{eventId}/process/{habitatId}` | Processar evento → alerta automático |

### Dashboard (RF08)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/v1/dashboard/habitat/{habitatId}` | Dashboard em tempo real |

### Métricas de Recursos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/v1/metrics` | Registrar métrica |
| `GET`  | `/api/v1/metrics/habitat/{id}/average` | Média de consumo (RN09) |
| `POST` | `/api/v1/metrics/habitat/{id}/analyze` | Análise + alertas automáticos |

---

## Cobertura de Requisitos

| Código | Requisito | Implementado |
|--------|-----------|:---:|
| RF01 | Monitorar O₂ | ✅ |
| RF02 | Atividade solar (NASA DONKI) | ✅ |
| RF03 | Alertas de meteoros (NASA NeoWs) | ✅ |
| RF04 | Consumo energético | ✅ |
| RF05 | Reservas de água | ✅ |
| RF06 | Produção agrícola | ✅ |
| RF07 | Alertas automáticos de emergência | ✅ |
| RF08 | Dashboard em tempo real | ✅ |
| RF09 | Histórico de eventos | ✅ |
| RF10 | APIs espaciais externas | ✅ |
| RN01 | Radiação alta → shields | ✅ |
| RN02 | O₂ baixo → alerta crítico | ✅ |
| RN03 | Meteoro → modo de defesa | ✅ |
| RN04 | Priorizar setores essenciais | ✅ |
| RN05 | Agricultura respeita água | ✅ |
| RN06 | Atualização periódica | ✅ |
| RN07 | Usuários sem acesso crítico | ✅ |
| RN08 | Ações automáticas logadas | ✅ |
| RN09 | Consumo médio calculado | ✅ |
| RN10 | Alertas críticos com prioridade | ✅ |

---

## Integrantes do Grupo

| Nome                 | RM        |
|----------------------|-----------|
| Henique Celso        | RM-559687 | 
| Lucas Cortizo        | RM-559734 | 
| Gabriela Queiroga    | RM-560035 |
| Maria Eduarda Ferrés | RM-560418 |

---

## Vídeo

- **Vídeo Pitch:** [link aqui]
- **Vídeo Técnico** [link aqui]

