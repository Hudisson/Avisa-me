package com.hudissonxavier.avisame.dto.users;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) para a resposta de dados do usuário.
 * Utilizado para customizar as informações que retornam da API, 
 * garantindo que dados sensíveis (como senhas) não sejam expostos ao cliente.
 */

@Data
@AllArgsConstructor
public class UserResponseDTO {
    
    // Identificador único do usuário
    private UUID id;

    // Nome público do usuário
    private String name;

    // E-mail do usuário para identificação e contato
    private String email;

    // Data e hora da quando foi criado
    private LocalDateTime UserCreatedAt;

    // Data e hora de quando foi atualizado
    private LocalDateTime UserUpdatedAt;
}
