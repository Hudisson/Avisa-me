package com.hudissonxavier.avisame.controller.agenda;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hudissonxavier.avisame.dto.agenda.UserConfigDTO;
import com.hudissonxavier.avisame.model.users.UserModel;
import com.hudissonxavier.avisame.service.agenda.UserConfigService;

import lombok.RequiredArgsConstructor;

/**
 * Controller para gerenciamento das preferências de notificação.
 * Permite definir o horário exato em que o usuário deseja receber o e-mail
 * diário.
 */

@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class UserConfigController {

    private final UserConfigService service;

    /**
     * Rota para salva o horário desejado para o envio dos e-mails
     * @param dto            Dados do evento a ser registrado no banco de dados
     * @param authentication Objeto de autenticação contendo o UserModel (token JWT)
     * @return 200 Ok
     */
    @PostMapping("/create")
    public ResponseEntity<?> createConfigHour(@RequestBody UserConfigDTO dto,
            Authentication authentication) {
        UserModel user = (UserModel) authentication.getPrincipal();

        // Sava a perefereência de horário do usuário
        service.saveConfig(dto, user);

        // Definir a mensagem de retorno
        HashMap<String, String> response = new HashMap<>();
        response.put("sucesso", "Preferência de horário definida com sucesso");
        
        // Retona 201 e a mensagem de retorno
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    /**
     * Rota para busca a configuração atual de agendamento do usuário
     * @param authentication Objeto de autenticação contendo o UserModel (token JWT)
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getConfig(Authentication authentication) {

        try {

            UserModel user = (UserModel) authentication.getPrincipal();
            UserConfigDTO config = service.findByUserId(user.getId());
            return ResponseEntity.ok(config);

        } catch (ResponseStatusException e) {

            // Retorna uma mensagem personalizada se o service lança 404
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "Você ainda não configurou um horário para receber as notificações!");
            response.put("status", "405"); // código de status definido pelo programado
            return ResponseEntity.ok(response);

        }

    }

    /**
     * Rota para atualizar o horário de configuração para receber as notifiações
     * @param dto
     * @param user
     * @return
     */
    @PutMapping("/update-time")
    public ResponseEntity<String> updateTime(@RequestBody UserConfigDTO dto, @AuthenticationPrincipal UserModel user) {

        service.updateNotificationTime(dto, user);
        return ResponseEntity.ok("Horário de notificação atualizado com sucesso!");

    }
}
