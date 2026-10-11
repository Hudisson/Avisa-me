package com.hudissonxavier.avisame.controller.users;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


/**
 * @RestController: Define que esta classe é um controlador REST.
 * @RequestMapping("/users"): Define que todos os endpoints deste controller começam com /users.
 */

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Rota para obter os dados do usuário autenticado (quem está logado)
     * @param authentication
     * @return
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(@AuthenticationPrincipal UserModel user) {
        return ResponseEntity.ok(userService.getById(user.getId()));
    }  
    
   
    /**
     * Rota Edita as informações do usuário logado.
     * @param dto
     * @param authentication
     * @return
     */
    @PutMapping("/edit")
    public ResponseEntity<?> editUser(
            @RequestBody UpdateUserRequestDTO dto,
            @AuthenticationPrincipal UserModel user
    ) {
        return ResponseEntity.ok(userService.updateProfile(user.getId(), dto));
    }

    /**
     * Rota para deleta a conta do usuário que está autenticado.
     * @param authentication
     * @return
     */
    @DeleteMapping("/delete-me")
    public ResponseEntity<Void> deleteMyAccount(@AuthenticationPrincipal UserModel user) {
        userService.delete(user.getId());
        return ResponseEntity.noContent().build();
    }

}