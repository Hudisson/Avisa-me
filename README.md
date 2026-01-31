# 🚀 Avisa-me: Sistema Inteligente de Notificações Diárias
O **Avisa-me** é uma aplicação Spring Boot desenvolvida para ajudar na organização pessoal e produtividade. O sistema permite que usuários agendem tarefas recorrentes e eventos pontuais, recebendo um resumo personalizado por e-mail no horário exato de sua preferência.

O diferencial do projeto é o motor de agendamento dinâmico que processa as filas de envio minuto a minuto, garantindo que cada usuário receba suas notificações de acordo com sua própria configuração de horário e rotina.

## **⚠️ Em desenvolvimento**...

## Rotas da API

### Criar e editar usuário

1) #### Rota para registrar um novo usuário.
Method: 🟢 POST <br>
   
`http://localhost:8080/auth/register`

- **Request Body** ( JSON ):
```json
{
    "name": "Nome do Usuário",
    "email": "email@example.com",
    "password": "senha123"
}
 ```
OBS.: *utilize um email válido*

- **Response** ( 201 Created ):
```json
{
  "sucesso", "Conta criada com sucesso"
}
```
- **Erros**:
  `409 Conflict`: Caso o e-mail já esteja em uso.
```json
{
    "erro": "O e-mail informado já está em uso"
}
```
<hr>

2) #### Rota para login de um usuário e obtenção de um token JWT.
Method: 🟢 POST <br> 
 
`http://localhost:8080/auth/login`
- **Request Body** ( JSON ):
```json
{
    "email": "email@example.com",
    "password": "senha123"
}
```
- **Response** ( 200 OK ):
    
```json
{
    "token": "JWT_token"
}
```
    
- **Erros**:
    
    - `401 Unauthorized`: Caso o e-mail ou senha estejam incorretos.
        
```json
{
    "erro": "Usuário ou senha inválidos"
}
```
3) #### Rota para obter dados do usuário logado
   Method: 🔵 GET <br>
   `http://localhost:8080/auth/login`
  - **Request body** ( JSON ):
    - Auth: Bearer Token ( JWT )
   
- **Response** ( 200 OK ):

```json
{
   "id": "UUID",
   "name": "Nome do usuário",
	"email": "exemplo@email.com",
	"userCreatedAt": "2026-01-12T10:55:26",
	"userUpdatedAt": "2026-01-12T12:11:01"
}
```
- **Erros**:
   - `403 forbidden `: No body returned for response

4) #### Rota para editar as informações do usuário
   Method: 🟡 PUT <br>
    `http://localhost:8080/users/edit`
   
    - Auth: Bearer Token ( JWT )
  
- **Response** ( 200 OK ):
   
```json
{
   "sucesso": "Informações atualizada com sucesso"
}
```
- **Erros**:
   - `403 forbidden `: No body returned for response

5) #### Deletar conta do usuário
      Method: 🔴 DELETE <br>
   `http://localhost:8080/users/delete-me`
   
- **Request body** ( JSON ):
  - Auth: Bearer Token ( JWT )
```json
{
   "id": "UUID-do-usuario"
}
```

- **Response** ( 204 No content ):

 <hr> 

### Criar e editar tarefas

6) #### Criar uma tarefa
   Method: 🟢 POST <br>
   `http://localhost:8080/tasks/create`
  - **Request body** ( JSON ):
     - Auth: Bearer Token ( JWT )

```json
{
  	"title": "Título da tarefa",
	"description": "Descrição da tarefa",
	"dayOfWeek": "SUNDAY",
	"isActive": true
}
```
  - **Response** ( 200 OK ):
```json
{
  "sucesso": "Tarefa criada com sucesso"
}
```
- **Erro** 403 forbidden


7) #### Editar uma tarefa
    Method: 🟡 PUT <br>
   `http://localhost:8080/tasks/edit/UUID-da-tarefa`
  - **Request body** ( JSON ):
     - Auth: Bearer Token ( JWT )
```json
{
  	"title": "Título",
	"description": "Descrição",
	"dayOfWeek": "WEDNESDAY",
	"isActive": true
}
```
  - **Response** ( 200 OK ):

```json
{
  "sucesso": "Tarefa editada com sucesso"
}
```
- **Erro** 403 forbidden

8) #### Listar todas as tarfas do usuário
    Method:🔵 GET <br>
   `http://localhost:8080/tasks/my-tasks`
  - **Request body** ( JSON ):
     - Auth: Bearer Token ( JWT )

  - **Response** ( 200 OK ):

```json
[
	{
		"id": "UUID-da-tarefa",
		"title": "Título da tarefa",
		"description": " Descrição da tarefa",
		"isActive": true,
		"dayOfWeek": "MONDAY",
		"taskCreatedAt": "2026-01-12T11:02:02",
		"taskUpdatedAt": "2026-01-12T08:02:02"
	},
]
```
- **Erro** 403 forbidden

9) #### Deletar uma tarefa
      Method: 🔴 DELETE <br>
   `http://localhost:8080/tasks/delete/UUID-da-tarefa`
   
- **Request body**:
 	 - Auth: Bearer Token ( JWT )

- **Response** ( 204 No content ):
- **Erro** 403 forbidden

 <hr> 

### Criar e editar eventos

10) #### Criar um evento
   Method: 🟢 POST <br>
   `http://localhost:8080/events/create`
  - **Request body** ( JSON ):
    - Auth: Bearer Token ( JWT )
```json
{
    "title": "Título do evento",
	"description": "Descrição do evento",
	"eventDate": "YYYY-MM-DD"
}
```
  - **Response** ( 200 OK ):
```json
{
  "sucesso": "Evento criado com sucesso"
}
```
- **Erro** 403 forbidden

11) #### Editar um evento
   Method: 🟡 PUT <br>
   `http://localhost:8080/events/edit/UUID-do-evento`
  - **Request body** ( JSON ):
     - Auth: Bearer Token ( JWT )
```json
{
    "title": "Título do evento",
	"description": "Descrição do evento",
	"eventDate": "YYYY-MM-DD"
}
```
  - **Response** ( 200 OK ):
```json
{
 "sucesso": "Evento editado com sucesso"
}
```
- **Erro** 403 forbidden

12) #### Listar todos os evento do usuário
    Method:🔵 GET <br>
   `http://localhost:8080/events/list`
  - **Request body** ( JSON ):
     - Auth: Bearer Token ( JWT )

  - **Response** ( 200 OK ):

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
	},
]
```
- **Erro** 403 forbidden

12) #### Listar todos os evento do dia atual do usuário
    Method:🔵 GET <br>
   `http://localhost:8080/events/today`
  - **Request body**:
     - Auth: Bearer Token ( JWT )

  - **Response** ( 200 OK ):

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

<br> caso não tenha eventos agendados

```json
{
	"date": "YYYY-MM-DD",
	"message": "Você não tem eventos agendado para hoje"
}
```
- **Erro** 403 forbidden

13) #### Deletar um evento do usuário
      Method: 🔴 DELETE <br>
   `http://localhost:8080/events/delete/UUID-do-evento`
   
- **Request body**:
   - Auth: Bearer Token ( JWT )

- **Response** ( 204 No content ):

 <hr> 

### Definir e editar horário para receber as notificações

14) #### Definir horário
   Method: 🟢 POST <br>
   `http://localhost:8080/config/create`
  - **Request body** ( JSON ):
    - Auth: Bearer Token ( JWT )
```json
{
	"preferredHour": "HH:MM" 
}
```
  - **Response** ( 201 Created ):
```json
{
  "sucesso": "Horário para receber as notificações definido com sucesso"
}
```
- **Erro** 403 forbidden

15) #### Buscar horário definido
    Method:🔵 GET <br>
   `http://localhost:8080/config`
  - **Request body**:
     - Auth: Bearer Token ( JWT )

  - **Response** ( 200 OK ):

```json
	"id": "UUID-do-horario",
	"user": null,
	"preferredHour": "12:50:00",
	"cronExpression": "0 50 12 * * *",
	"configCreatedAt": "2026-01-12T14:42:23",
	"configUpdatedAt": "2026-01-25T12:49:07"
```

- **Erro** 403 forbidden

16) #### Editar horário das notificações
   Method: 🟡 PUT <br>
   `http://localhost:8080/config/update-time`
  - **Request body** ( JSON ):
     - Auth: Bearer Token ( JWT )
```json
{
  "preferredHour": "HH:MM"
}
```
  - **Response** ( 200 OK ):
```json
{
 "sucesso": "Horário de notificação atualizado com sucesso!"
}
```
- **Erro** 403 forbidden

 <hr> 

### Logout

17) #### Deslogar (Logout)
   Method: 🟢 POST <br>
   `http://localhost:8080/auth/logout`
  - **Request body**:
    - Auth: Bearer Token ( JWT )

  - **Response** ( 204 No content ):
