package com.hudissonxavier.avisame.repository.agenda;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hudissonxavier.avisame.model.agenda.TokenBlacklistModel;

/**
 * Camada de acesso a dados para a Blacklist de Tokens.
 * Responsável por gerenciar a persistência de tokens JWT que foram
 * invalidados durante o processo de logout ou por questões de segurança.
 */

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklistModel, UUID> {
    
    /**
     * Verifica se um determinado token JWT consta na lista negra.
     * Este método é crucial para os filtros de segurança (Security Filters)
     * decidirem se permitem ou bloqueiam uma requisição.
     * * @param token O JWT enviado no cabeçalho da requisição.
     * @return true se o token estiver na blacklist (inválido), false caso contrário.
     */
    boolean existsByToken(String token);
}
