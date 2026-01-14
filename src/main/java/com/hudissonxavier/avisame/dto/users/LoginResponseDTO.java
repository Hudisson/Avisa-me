package com.hudissonxavier.avisame.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) para resposta de autenticação bem-sucedida.
 * Encapsula o token de acesso que será utilizado pelo cliente para 
 * autorizar requisições subsequentes aos recursos protegidos da API.
 */

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    
    /** * Token de acesso no formato JWT.
     * Deve ser enviado pelo cliente no cabeçalho 'Authorization' como um 'Bearer token'.
     */
    private String token;
}
