package com.hudissonxavier.avisame.controller.users;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hudissonxavier.avisame.dto.users.UpdateUserRequestDTO;
import com.hudissonxavier.avisame.dto.users.UserResponseDTO;
import com.hudissonxavier.avisame.model.users.UserModel;
import com.hudissonxavier.avisame.service.users.UserService;

import lombok.RequiredArgsConstructor;

/**
 * @RestController: Define que esta classe é um controlador REST.
 * @RequestMapping("/users"): Define que todos os endpoints deste controller começam com /users.
 * @RequiredArgsConstructor: Cria automaticamente um construtor para campos declarados como 'final' (injeção de dependência).
 */

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    // Dependência de serviço injetada automaticamente pelo Lombok/Spring
    private final UserService userService;

    /**
     * Rota para obter os dados do usuário autenticado (quem está logado)
     * @param authentication
     * @return
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(Authentication authentication) {

        // Recupera o objeto de usuário que o Spring Security armazenou na sessão/token
        UserModel user = (UserModel) authentication.getPrincipal();

        // Transforma o modelo do banco em um DTO (objeto de transferência) para esconder dados sensíveis como senha
        UserResponseDTO response = new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getUserCreatedAt(), user.getUserUpdatedAt());
        return ResponseEntity.ok(response);
    }  
    
   
    /**
     * Rota Edita as informações do usuário logado.
     * @param dto
     * @param authentication
     * @return
     */
    @PutMapping("/edit")
    public ResponseEntity<UserResponseDTO> editUser(
            @RequestBody UpdateUserRequestDTO dto, // Dados novos vindos do corpo da requisição (JSON)
            Authentication authentication // Token com os dados do usuário logado
    ) {
        UserModel currentUser = (UserModel) authentication.getPrincipal();

        // SEGURANÇA: Verifica se o ID do usuário logado é o mesmo ID do usuário que ele quer editar
        if (!currentUser.getId().equals(dto.getId())) {

            // Se tentar editar o perfil de outra pessoa, retorna 403 (Proibido)
            return ResponseEntity.status(403).build(); // Forbidden
        }

        // Se a verificação passar, envia para a camada de serviço realizar o update
        return ResponseEntity.ok(userService.update(dto.getId(), dto));
    }

   
    /**
     * Rota para deleta a conta do usuário que está autenticado.
     * @param authentication
     * @return
     */
    @DeleteMapping("/delete-me")
    public ResponseEntity<Void> deleteMyAccount(Authentication authentication) {
        UserModel currentUser = (UserModel) authentication.getPrincipal();

        // Remove o usuário do banco de dados usando o ID autenticado
        userService.delete(currentUser.getId());

        // Retorna Status 204 No Content (sucesso, mas sem corpo na resposta)
        return ResponseEntity.noContent().build();
    }


    

}
