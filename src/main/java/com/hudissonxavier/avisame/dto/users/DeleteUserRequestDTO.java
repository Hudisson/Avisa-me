package com.hudissonxavier.avisame.dto.users;

import java.util.UUID;

import lombok.Data;

/**
 * Data Transfer Object (DTO) para solicitação de exclusão de usuário.
 * Utilizado para capturar o identificador único do registro que deve ser 
 * removido do banco de dados, garantindo uma operação precisa e segura.
 */

@Data
public class DeleteUserRequestDTO {
    
    // Identificador único (UUID) do usuário a ser deletado
    private UUID id;
}
