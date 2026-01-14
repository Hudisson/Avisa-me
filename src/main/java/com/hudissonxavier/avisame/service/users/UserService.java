package com.hudissonxavier.avisame.service.users;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hudissonxavier.avisame.dto.users.UpdateUserRequestDTO;
import com.hudissonxavier.avisame.dto.users.UserRequestDTO;
import com.hudissonxavier.avisame.dto.users.UserResponseDTO;
import com.hudissonxavier.avisame.model.users.UserModel;
import com.hudissonxavier.avisame.repository.users.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Camada de serviço para gerenciamento de usuários (CRUD).
 * Centraliza as regras de negócio, validações de integridade e a 
 * orquestração entre Repositories e DTOs.
 */

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository repository;
    private final PasswordEncoder encoder;


    /**
     * Registra um novo usuário no sistema.
     * Valida a duplicidade de e-mail e realiza a criptografia da senha antes de persistir.
     * @param dto Dados de entrada para criação.
     * @return UserResponseDTO Dados públicos do usuário criado.
     * @throws ResponseStatusException 409 se o e-mail já estiver em uso.
     */
    public UserResponseDTO create(UserRequestDTO dto) {

        // Regra de Negócio: Não permitir e-mails duplicados - verifica se o e-mail já existe
        if (repository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        // Mapeamento de DTO para Entity com criptografia de senha - criação do novo usuário
        UserModel user = UserModel.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build();

        // Salva o novo usuário no banco
        repository.save(user);

        // Retorna a resposta com os dados do usuário
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getUserCreatedAt(), user.getUserUpdatedAt());
    }

    /**
     * Atualiza parcialmente os dados de um usuário existente.
     * Verifica permissões de e-mail e aplica novas senhas se fornecidas.
     * @param id ID do usuário a ser editado.
     * @param dto Dados de atualização.
     * @return UserResponseDTO Dados atualizados.
     */
    public UserResponseDTO update(UUID id, UpdateUserRequestDTO dto) {
        UserModel user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        // Atualização seletiva: só altera o que não for nulo ou vazio
        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            repository.findByEmail(dto.getEmail()).ifPresent(existingUser -> {

                // Se o e-mail pertence a OUTRO usuário, lança erro
                if (!existingUser.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já em uso");
                }
            });
            user.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(encoder.encode(dto.getPassword()));
        }

        repository.save(user);
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(),user.getUserCreatedAt(), user.getUserUpdatedAt());
    }

    /**
     * Remove um usuário permanentemente do sistema.
     * @param id UUID do usuário.
     * @throws ResponseStatusException 404 se o usuário não for encontrado.
     */
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        repository.deleteById(id);
    }
}
