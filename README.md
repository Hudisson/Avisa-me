# 🚀 Avisa-me: Sistema Inteligente de Notificações Diárias
O **Avisa-me** é uma aplicação Spring Boot desenvolvida para ajudar na organização pessoal e produtividade. O sistema permite que usuários agendem tarefas recorrentes e eventos pontuais, recebendo um resumo personalizado por e-mail no horário exato de sua preferência.

O diferencial do projeto é o motor de agendamento dinâmico que processa as filas de envio minuto a minuto, garantindo que cada usuário receba suas notificações de acordo com sua própria configuração de horário e rotina.

## **⚠️ Em desenvolvimento**...

## Rotas da API
1) #### Rota para registrar um novo usuário.
Method: 🟢 POST <br>
   
`http://localhost:8080/auth/register`

- **Request Body** (JSON):
```json
{
    "name": "Nome do Usuário",
    "email": "email@example.com",
    "password": "senha123"
}
 ```
OBS.: *utilise um email válido*

- **Response** (Status: 201 Created):
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
- **Request Body** (JSON):
```json
{
    "email": "email@example.com",
    "password": "senha123"
}
```
- **Response** (Status: 200 OK):
    
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

