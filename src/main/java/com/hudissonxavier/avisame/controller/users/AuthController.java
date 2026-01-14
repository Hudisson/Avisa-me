package com.hudissonxavier.avisame.controller.users;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hudissonxavier.avisame.dto.users.LoginRequestDTO;
import com.hudissonxavier.avisame.dto.users.LoginResponseDTO;
import com.hudissonxavier.avisame.dto.users.UserRequestDTO;
import com.hudissonxavier.avisame.dto.users.UserResponseDTO;
import com.hudissonxavier.avisame.service.users.AuthService;
import com.hudissonxavier.avisame.service.users.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Recurso REST para gestão de acesso e identidade.
 * Disponibiliza endpoints para criação de conta (registro), 
 * autenticação (login) e encerramento de sessão (logout).
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    // Dependências de serviços injetada automaticamente pelo Lombok/Spring
    private final AuthService authService;
    private final UserService userService;

    /**
     * Rota para cadastro de novos usuários.
     * @param dto Objeto contendo nome, e-mail e senha.
     * @return 201 Created com os dados do usuário ou 409 Conflict se o e-mail existir.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO dto) {

        try {

            // Delega a criação para o serviço de usuário - Tenta criar o usuário
            UserResponseDTO response = userService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        }catch (ResponseStatusException ex){

            /**
             * Tratamento customizado para conflito de e-mail duplicado.
             * Se o e-mail já existe, retorna conflito (409) com um JSON simples
             */
            HashMap<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.CONFLICT.value());
            response.put("message", "O e-mail informado já está em uso");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    /**
     * Rota para autenticação de usuários existentes.
     * @param dto Credenciais de acesso (e-mail e senha).
     * @return 200 OK com o Token JWT ou 401 Unauthorized para credenciais inválidas.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {

        try {

            // Valida credenciais e gera o token de acesso
            LoginResponseDTO response = authService.login(dto);
            return ResponseEntity.ok(response);

        }catch (ResponseStatusException ex){

            /**
             * Retorno padronizado para falhas de autenticação.
             * Caso as credenciais estejam incorretas, retorna um JSON simples com status e mensagem
             */ 
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("message", "E-mail ou senha inválidos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

    }

    /**
     * Rota para encerramento de sessão.
     * @param header Cabeçalho de autorização contendo o token 'Bearer '.
     * @return 204 No Content após invalidar o token com sucesso.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String header) {
        // Extrai o token removendo o prefixo "Bearer " e envia para a blacklist
        authService.logout(header.substring(7));
        return ResponseEntity.noContent().build();
    }
    
}
