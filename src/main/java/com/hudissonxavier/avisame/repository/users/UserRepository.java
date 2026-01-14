package com.hudissonxavier.avisame.repository.users;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hudissonxavier.avisame.model.users.UserModel;

/**
 * Interface de repositório para a entidade UserModel.
 * Fornece a abstração necessária para a manipulação de dados no banco, 
 * utilizando o Spring Data JPA para geração automática de consultas.
 * * Responsabilidades:
 * - Persistência e recuperação de usuários.
 * - Consultas customizadas (Derived Queries) para busca por e-mail.
 * - Verificação de existência de registros para evitar duplicidade.
 */

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    
    /**
     * Busca um usuário através do e-mail informado.
     * @param email E-mail do usuário.
     * @return Um Optional contendo o usuário se encontrado, ou vazio caso contrário.
     */
    Optional<UserModel> findByEmail(String email);

    /**
     * Verifica se já existe um usuário cadastrado com o e-mail informado.
     * Útil para validações de novo cadastro.
     * @param email E-mail a ser verificado.
     * @return true se o e-mail já existir, false caso contrário.
     */
    boolean existsByEmail(String email);
}
