package com.hudissonxavier.avisame.dto.users;

import lombok.Data;

/**
 * Data Transfer Object (DTO) para captura de credenciais de acesso.
 * Utilizado no endpoint de login para receber as informações de 
 * identificação do usuário que deseja iniciar uma sessão.
 */

@Data
public class LoginRequestDTO {
    
    // E-mail cadastrado do usuário que servirá como login
    private String email;

    // Senha em texto puro, enviada para conferência com o hash do banco de dados
    private String password;
}
