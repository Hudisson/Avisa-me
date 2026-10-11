# 🚀 Avisa-me: Sistema Inteligente de Notificações Diárias

O **Avisa-me** é uma API desenvolvida em Java com Spring Boot para ajudar na organização pessoal e produtividade. O sistema permite que usuários agendem **tarefas recorrentes** (por dia da semana) e **eventos pontuais** (por data específica), recebendo um resumo personalizado por e-mail no horário exato de sua preferência.

O diferencial do projeto é o motor de agendamento dinâmico: cada usuário define seu próprio horário de notificação (rota `/config`), que é convertido em uma expressão cron individual, permitindo que cada um receba seu resumo diário na hora que escolher — sem depender de um horário fixo global para todos.

> ⚠️ **Em desenvolvimento**

## Índice

- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Rodando com Docker Compose](#rodando-com-docker-compose-recomendado)
- [Rodando localmente sem Docker](#rodando-localmente-sem-docker)
- [Variáveis de ambiente](#variáveis-de-ambiente)
- [Autenticação](#autenticação)
- [Referência rápida das rotas](#referência-rápida-das-rotas)
- [Rotas da API (detalhado)](#rotas-da-api)

## Tecnologias

- **Java 21**
- **Spring Boot 3.5.9**
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Spring Mail
- **MySQL 8**
- **JWT** ([`jjwt`](https://github.com/jwtk/jjwt)) para autenticação stateless
- **Lombok**
- **Maven** (com Maven Wrapper — `mvnw` / `mvnw.cmd`)
- **Docker** e **Docker Compose** para orquestração local (API + banco de dados)

## Pré-requisitos

Escolha um dos dois caminhos abaixo:

- **Com Docker (recomendado):** [Docker](https://www.docker.com/) e Docker Compose — não precisa instalar Java nem MySQL na sua máquina.
- **Sem Docker:** [JDK 21](https://adoptium.net/) e um [MySQL 8](https://dev.mysql.com/downloads/mysql/) rodando localmente.

## Rodando com Docker Compose (recomendado)

O `docker-compose.yml` já sobe dois serviços: o banco de dados MySQL e a API.

```bash
git clone https://github.com/Hudisson/Avisa-me.git
cd Avisa-me
docker compose up --build
```

- API disponível em `http://localhost:8080`
- MySQL exposto na porta `3304` (mapeada para a `3306` interna do container)

> **Antes de subir em qualquer ambiente compartilhado:** o `docker-compose.yml` vem com valores de exemplo para senha do banco, segredo JWT e credenciais de e-mail. Troque esses valores (veja a seção [Variáveis de ambiente](#variáveis-de-ambiente)) antes de expor a aplicação além do seu próprio ambiente local.

## Rodando localmente sem Docker

1. Suba um MySQL 8 local (ou reaproveite só o serviço de banco do compose: `docker compose up db`) e crie um banco de dados.
2. Defina as [variáveis de ambiente](#variáveis-de-ambiente) necessárias (ou configure um `application.properties`/`application.yml` local com os mesmos valores).
3. Rode a aplicação com o Maven Wrapper:

```bash
./mvnw spring-boot:run
```

(no Windows, use `mvnw.cmd spring-boot:run`)

## Variáveis de ambiente

| Variável | Descrição | Exemplo |
|---|---|---|
| `SPRING_DATASOURCE_URL` | URL de conexão JDBC com o MySQL | `jdbc:mysql://localhost:3306/dbavisame?useTimezone=true&serverTimezone=America/Sao_Paulo` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco de dados | `user_avisame` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco de dados | — |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Estratégia de atualização do schema pelo Hibernate | `update` |
| `JWT_SECRET` | Chave usada para assinar os tokens JWT | Gere uma chave segura própria |
| `MAIL_HOST` | Servidor SMTP para envio dos e-mails de notificação | `smtp.gmail.com` |
| `MAIL_PORT` | Porta do servidor SMTP | `587` |
| `MAIL_USERNAME` | E-mail usado para autenticar no servidor SMTP | — |
| `MAIL_PASSWORD` | Senha (ou senha de app) do e-mail acima | — |
| `TZ` | Fuso horário da aplicação | `America/Sao_Paulo` |

> Nunca reutilize os valores de exemplo do `docker-compose.yml` em produção — especialmente `JWT_SECRET` e as credenciais de e-mail/banco.

## Autenticação

A API usa autenticação **stateless via JWT**:

1. Crie uma conta em `POST /auth/register` e faça login em `POST /auth/login` para obter um token.
2. Envie esse token em todas as rotas protegidas, no header:

```
Authorization: Bearer <seu-token-jwt>
```

3. Rotas sem token válido (ou com token de outro usuário tentando acessar um recurso que não é seu) retornam `403 Forbidden`.

## Referência rápida das rotas

| Recurso | Método | Rota | Autenticado |
|---|---|---|---|
| Registrar usuário | 🟢 POST | `/auth/register` | Não |
| Login | 🟢 POST | `/auth/login` | Não |
| Logout | 🟢 POST | `/auth/logout` | Sim |
| Dados do usuário logado | 🔵 GET | `/users/me` | Sim |
| Editar usuário | 🟡 PUT | `/users/edit` | Sim |
| Excluir conta | 🔴 DELETE | `/users/delete-me` | Sim |
| Criar tarefa | 🟢 POST | `/tasks/create` | Sim |
| Listar tarefas do usuário | 🔵 GET | `/tasks/my-tasks` | Sim |
| Buscar tarefa por ID | 🔵 GET | `/tasks/{id}` | Sim |
| Editar tarefa | 🟡 PUT | `/tasks/edit/{id}` | Sim |
| Excluir tarefa | 🔴 DELETE | `/tasks/delete/{id}` | Sim |
| Criar evento | 🟢 POST | `/events/create` | Sim |
| Listar eventos do usuário | 🔵 GET | `/events/list` | Sim |
| Listar eventos de hoje | 🔵 GET | `/events/today` | Sim |
| Buscar evento por ID | 🔵 GET | `/events/{id}` | Sim |
| Editar evento | 🟡 PUT | `/events/edit/{id}` | Sim |
| Excluir evento | 🔴 DELETE | `/events/delete/{id}` | Sim |
| Definir horário de notificação | 🟢 POST | `/config/create` | Sim |
| Buscar horário definido | 🔵 GET | `/config` | Sim |
| Editar horário de notificação | 🟡 PUT | `/config/update-time` | Sim |

## Rotas da API

### Usuários e autenticação

#### 1. Registrar um novo usuário

Método: 🟢 POST
`http://localhost:8080/auth/register`

- **Request Body** (JSON):
```json
{
    "name": "Nome do Usuário",
    "email": "email@example.com",
    "password": "senha123"
}
```
> Utilize um e-mail válido.

- **Response** (201 Created):
```json
{
  "sucesso": "Conta criada com sucesso"
}
```
- **Erros:**
  - `409 Conflict` — e-mail já em uso:
```json
{
    "erro": "O e-mail informado já está em uso"
}
```

#### 2. Login (obter token JWT)

Método: 🟢 POST
`http://localhost:8080/auth/login`

- **Request Body** (JSON):
```json
{
    "email": "email@example.com",
    "password": "senha123"
}
```
- **Response** (200 OK):
```json
{
    "token": "JWT_token"
}
```
- **Erros:**
  - `401 Unauthorized` — e-mail ou senha incorretos:
```json
{
    "erro": "Usuário ou senha inválidos"
}
```

#### 3. Dados do usuário logado

Método: 🔵 GET
`http://localhost:8080/users/me`
Auth: Bearer Token (JWT)

- **Response** (200 OK):
```json
{
   "id": "UUID",
   "name": "Nome do usuário",
   "email": "exemplo@email.com",
   "userCreatedAt": "2026-01-12T10:55:26",
   "userUpdatedAt": "2026-01-12T12:11:01"
}
```
- **Erros:** `403 Forbidden` (sem corpo na resposta)

#### 4. Editar informações do usuário

Método: 🟡 PUT
`http://localhost:8080/users/edit`
Auth: Bearer Token (JWT)

- **Response** (200 OK):
```json
{
   "sucesso": "Informações atualizada com sucesso"
}
```
- **Erros:** `403 Forbidden` (sem corpo na resposta)

#### 5. Excluir conta do usuário

Método: 🔴 DELETE
`http://localhost:8080/users/delete-me`
Auth: Bearer Token (JWT)

- **Request Body** (JSON):
```json
{
   "id": "UUID-do-usuario"
}
```
- **Response:** `204 No Content`

---

### Tarefas

#### 6. Criar uma tarefa

Método: 🟢 POST
`http://localhost:8080/tasks/create`
Auth: Bearer Token (JWT)

- **Request Body** (JSON):
```json
{
    "title": "Título da tarefa",
    "description": "Descrição da tarefa",
    "dayOfWeek": "SUNDAY",
    "isActive": true
}
```
- **Response** (200 OK):
```json
{
  "sucesso": "Tarefa criada com sucesso"
}
```
- **Erros:** `403 Forbidden`

#### 7. Listar todas as tarefas do usuário

Método: 🔵 GET
`http://localhost:8080/tasks/my-tasks`
Auth: Bearer Token (JWT)

- **Response** (200 OK):
```json
[
	{
		"id": "UUID-da-tarefa",
		"title": "Título da tarefa",
		"description": "Descrição da tarefa",
		"isActive": true,
		"dayOfWeek": "MONDAY",
		"taskCreatedAt": "2026-01-12T11:02:02",
		"taskUpdatedAt": "2026-01-12T08:02:02"
	}
]
```
- **Erros:** `403 Forbidden`

#### 8. Buscar uma tarefa por ID

Método: 🔵 GET
`http://localhost:8080/tasks/{id}`
Auth: Bearer Token (JWT)

- **Response** (200 OK):
```json
{
	"id": "UUID-da-tarefa",
	"title": "Título da tarefa",
	"description": "Descrição da tarefa",
	"isActive": true,
	"dayOfWeek": "MONDAY",
	"taskCreatedAt": "2026-01-12T11:02:02",
	"taskUpdatedAt": "2026-01-12T08:02:02"
}
```
- **Erros:**
  - `404 Not Found` — tarefa não existe
  - `403 Forbidden` — tarefa existe, mas não pertence ao usuário logado

#### 9. Editar uma tarefa

Método: 🟡 PUT
`http://localhost:8080/tasks/edit/{id}`
Auth: Bearer Token (JWT)

- **Request Body** (JSON):
```json
{
    "title": "Título",
    "description": "Descrição",
    "dayOfWeek": "WEDNESDAY",
    "isActive": true
}
```
- **Response** (200 OK):
```json
{
  "sucesso": "Tarefa editada com sucesso"
}
```
- **Erros:** `403 Forbidden`

#### 10. Excluir uma tarefa

Método: 🔴 DELETE
`http://localhost:8080/tasks/delete/{id}`
Auth: Bearer Token (JWT)

- **Response:** `204 No Content`
- **Erros:** `403 Forbidden`

---

### Eventos

#### 11. Criar um evento

Método: 🟢 POST
`http://localhost:8080/events/create`
Auth: Bearer Token (JWT)

- **Request Body** (JSON):
```json
{
    "title": "Título do evento",
    "description": "Descrição do evento",
    "eventDate": "YYYY-MM-DD"
}
```
- **Response** (200 OK):
```json
{
  "sucesso": "Evento criado com sucesso"
}
```
- **Erros:** `403 Forbidden`

#### 12. Listar todos os eventos do usuário

Método: 🔵 GET
`http://localhost:8080/events/list`
Auth: Bearer Token (JWT)

- **Response** (200 OK):
```json
[
	{
		"id": "UUID-do-evento",
		"title": "Título do evento",
		"description": "Descrição do evento",
		"eventDate": "YYYY-MM-DD",
		"isNotified": false,
		"eventCreatedAt": "2026-01-25T11:44:00",
		"eventUpdatedAt": "2026-01-25T12:04:33"
	}
]
```
> Quando não há eventos cadastrados, a resposta é `200 OK` com `{ "message": "Você não tem eventos agendados" }` em vez de uma lista vazia.

- **Erros:** `403 Forbidden`

#### 13. Listar os eventos do dia atual

Método: 🔵 GET
`http://localhost:8080/events/today`
Auth: Bearer Token (JWT)

- **Response** (200 OK):
```json
[
	{
		"id": "UUID-do-evento",
		"title": "Título do evento",
		"description": "Descrição do evento",
		"eventDate": "YYYY-MM-DD",
		"isNotified": false,
		"eventCreatedAt": "2026-01-12T17:27:40",
		"eventUpdatedAt": "2026-01-31T22:18:14"
	}
]
```
Caso não haja eventos agendados para hoje:
```json
{
	"date": "YYYY-MM-DD",
	"message": "Você não tem eventos agendado para hoje"
}
```
- **Erros:** `403 Forbidden`

#### 14. Buscar um evento por ID

Método: 🔵 GET
`http://localhost:8080/events/list/{id}`
Auth: Bearer Token (JWT)

- **Response** (200 OK):
```json
{
	"id": "UUID-do-evento",
	"title": "Título do evento",
	"description": "Descrição do evento",
	"eventDate": "YYYY-MM-DD",
	"isNotified": false,
	"eventCreatedAt": "2026-01-25T11:44:00",
	"eventUpdatedAt": "2026-01-25T12:04:33"
}
```
- **Erros:**
  - `404 Not Found` — evento não existe
  - `403 Forbidden` — evento existe, mas não pertence ao usuário logado

#### 15. Editar um evento

Método: 🟡 PUT
`http://localhost:8080/events/edit/{id}`
Auth: Bearer Token (JWT)

- **Request Body** (JSON):
```json
{
    "title": "Título do evento",
    "description": "Descrição do evento",
    "eventDate": "YYYY-MM-DD"
}
```
- **Response** (200 OK):
```json
{
 "sucesso": "Evento editado com sucesso"
}
```
- **Erros:** `403 Forbidden`

#### 16. Excluir um evento

Método: 🔴 DELETE
`http://localhost:8080/events/delete/{id}`
Auth: Bearer Token (JWT)

- **Response:** `204 No Content`
- **Erros:** `403 Forbidden`

---

### Configuração de horário das notificações

#### 17. Definir horário

Método: 🟢 POST
`http://localhost:8080/config/create`
Auth: Bearer Token (JWT)

- **Request Body** (JSON):
```json
{
	"preferredHour": "HH:MM"
}
```
- **Response** (201 Created):
```json
{
  "sucesso": "Horário para receber as notificações definido com sucesso"
}
```
- **Erros:** `403 Forbidden`

#### 18. Buscar horário definido

Método: 🔵 GET
`http://localhost:8080/config`
Auth: Bearer Token (JWT)

- **Response** (200 OK):
```json
{
	"id": "UUID-do-horario",
	"user": null,
	"preferredHour": "12:50:00",
	"cronExpression": "0 50 12 * * *",
	"configCreatedAt": "2026-01-12T14:42:23",
	"configUpdatedAt": "2026-01-25T12:49:07"
}
```
- **Erros:** `403 Forbidden`

#### 19. Editar horário das notificações

Método: 🟡 PUT
`http://localhost:8080/config/update-time`
Auth: Bearer Token (JWT)

- **Request Body** (JSON):
```json
{
  "preferredHour": "HH:MM"
}
```
- **Response** (200 OK):
```json
{
 "sucesso": "Horário de notificação atualizado com sucesso!"
}
```
- **Erros:** `403 Forbidden`

---

### Logout

#### 20. Deslogar

Método: 🟢 POST
`http://localhost:8080/auth/logout`
Auth: Bearer Token (JWT)

- **Response:** `204 No Content`

---

Desenvolvido por [Hudisson Xavier](https://github.com/hudisson) · [LinkedIn](https://linkedin.com/in/hudisson-xavier)
