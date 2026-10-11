package com.hudissonxavier.avisame.controller.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hudissonxavier.avisame.dto.users.LoginRequestDTO;
import com.hudissonxavier.avisame.dto.users.LoginResponseDTO;
import com.hudissonxavier.avisame.dto.users.UserRequestDTO;
import com.hudissonxavier.avisame.service.users.AuthService;
import com.hudissonxavier.avisame.service.users.UserService;


/**
 * Recurso REST para gestão de acesso e identidade.
 * Disponibiliza endpoints para criação de conta (registro), 
 * autenticação (login) e encerramento de sessão (logout).
 */

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * Rota para cadastro de novos usuários.
     * @param dto Objeto contendo nome, e-mail e senha.
     * @return 201 Created com os dados do usuário ou 409 Conflict se o e-mail existir.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

    /**
     * Rota para autenticação de usuários existentes.
     * @param dto Credenciais de acesso (e-mail e senha).
     * @return 200 OK com o Token JWT ou 401 Unauthorized para credenciais inválidas.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Rota para encerramento de sessão.
     * @param header Cabeçalho de autorização contendo o token 'Bearer '.
     * @return 204 No Content após invalidar o token com sucesso.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String header) {
        authService.logout(header);
        return ResponseEntity.noContent().build();
    }
    
}