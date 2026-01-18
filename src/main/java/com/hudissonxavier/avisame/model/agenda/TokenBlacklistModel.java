package com.hudissonxavier.avisame.model.agenda;

import java.sql.Types;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa a entidade de Blacklist de Tokens (Segurança).
 * Armazena temporariamente os tokens JWT que foram invalidados (ex: após o Logout) 
 * para impedir que sejam reutilizados antes de sua expiração natural.
 * * Responsabilidades:
 * - Persistência de tokens revogados.
 * - Suporte ao controle de autenticação stateless.
 * - Garantia de que sessões encerradas não sejam mais acessíveis.
 */

@Entity
@Table(name = "tb_token_blacklist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenBlacklistModel {
    
    /**Atributos */

    // Identificador da entrada na blacklist
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "id")
    private UUID id;

    /** * O token JWT invalidado. 
     * Definido com 500 caracteres para comportar a estrutura extensa de um JWT. 
     */
    @Column(length = 500, nullable = false)
    private String token;

}
