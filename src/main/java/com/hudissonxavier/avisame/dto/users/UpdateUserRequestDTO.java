package com.hudissonxavier.avisame.dto.users;

import java.util.UUID;

import lombok.Data;

/**
 * Data Transfer Object (DTO) para atualização de dados de um usuário existente.
 * Utilizado para receber as modificações enviadas pelo cliente, permitindo 
 * a alteração parcial ou total do perfil.
 */

@Data
public class UpdateUserRequestDTO {

    // Identificador único do usuário que sofrerá a atualização
    private UUID id;

    // Novo nome do usuário (se houver alteração)
    private String name;

    // Novo e-mail do usuário (se houver alteração)
    private String email;

    /** * Nova senha do usuário.
     * Campo opcional: geralmente tratado na lógica de serviço para ser 
     * atualizado apenas se for enviado um valor não nulo.
     */
    private String password;
}
