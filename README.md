# 🚀 Avisa-me: Sistema Inteligente de Notificações Diárias
O **Avisa-me** é uma aplicação Spring Boot desenvolvida para ajudar na organização pessoal e produtividade. O sistema permite que usuários agendem tarefas recorrentes e eventos pontuais, recebendo um resumo personalizado por e-mail no horário exato de sua preferência.

O diferencial do projeto é o motor de agendamento dinâmico que processa as filas de envio minuto a minuto, garantindo que cada usuário receba suas notificações de acordo com sua própria configuração de horário e rotina.

## **⚠️ Em desenvolvimento**...

## Rotas da API
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
OBS.: *utilise um email válido*

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
    - Auth: Berarer Token ( JWT )
   
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
   
    - Auth: Berarer Token ( JWT )
  
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
 - Auth: Berarer Token ( JWT )
```json
{
   "id": "UUID"
}
```

- **Response** ( 204 No content ):

6) #### Criar uma tarefa
   Method: 🟢 POST <br>
   `http://localhost:8080/tasks/create`
  - **Request body** ( JSON ):
    - Auth: Berarer Token ( JWT )
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
    - Auth: Berarer Token ( JWT )
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
    - Auth: Berarer Token ( JWT )

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
