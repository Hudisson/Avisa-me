package com.hudissonxavier.avisame.model.users;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
 * Representa a entidade de usuário no sistema (Domain Model).
 * Esta classe é mapeada para a tabela 'tb_users' no banco de dados e 
 * utiliza UUID como identificador único para garantir maior segurança e escalabilidade.
 * * Responsabilidades:
 * - Mapeamento Objeto-Relacional (ORM) com JPA/Hibernate.
 * - Definição de restrições de integridade (e-mail único, campos obrigatórios).
 * - Fornecimento de métodos de acesso e construção via Lombok.
 */

@Entity
@Table(name = "tb_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel implements UserDetails{
    
    /**Atributos */

    // Identificador único do usuário gerado automaticamente (UUID)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "user_id")
    private UUID id;

    // Nome completo do usuário - Campo obrigatório
    @Column(name = "user_name", nullable = false)
    String name;

    // E-mail para login - Deve ser único no sistema e obrigatório
    @Column(name = "user_email", unique = true, nullable = false)
    String email;

    // Senha criptografada do usuário - Campo obrigatório
    @Column(name = "user_password", nullable = false)
    private String password;

    // Data e hora da quando foi criado
    @Column(name = "user_created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime UserCreatedAt;

    // Data e hora de quando foi atualizado
    @Column(name = "user_updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime UserUpdatedAt;


    /**Métodos */

    // Usado no JwtFilter
    @Override
    public String getUsername() {
        return this.email; 
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    // Mantenha os outros métodos (isAccountNonExpired, etc) retornando true
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
