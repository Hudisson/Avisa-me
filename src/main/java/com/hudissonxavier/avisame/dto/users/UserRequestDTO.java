package com.hudissonxavier.avisame.dto.users;

import lombok.Data;

/**
 * Data Transfer Object (DTO) para a criação de novos usuários.
 * Captura os dados enviados pelo cliente no corpo da requisição (Payload)
 * durante o processo de registro/cadastro.
 */

@Data
public class UserRequestDTO {
    
    // Nome do usuário a ser cadastrado
    private String name;

    // E-mail que será utilizado como identificador de login
    private String email;

    // Senha em texto puro (deve ser criptografada antes de persistir no banco)
    private String password;

}
