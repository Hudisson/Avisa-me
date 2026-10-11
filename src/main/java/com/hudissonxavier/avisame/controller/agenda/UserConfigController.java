package com.hudissonxavier.avisame.controller.agenda;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hudissonxavier.avisame.dto.agenda.UserConfigDTO;
import com.hudissonxavier.avisame.model.users.UserModel;
import com.hudissonxavier.avisame.service.agenda.UserConfigService;


/**
 * Controller para gerenciamento das preferências de notificação.
 * Permite definir o horário exato em que o usuário deseja receber o e-mail
 * diário.
 */

@RestController
@RequestMapping("/config")
public class UserConfigController {

    private final UserConfigService service;

    public UserConfigController(UserConfigService service) {
        this.service = service;
    }

    /**
     * Rota para salva o horário desejado para o envio dos e-mails
     * @param dto            Dados do evento a ser registrado no banco de dados
     * @param authentication Objeto de autenticação contendo o UserModel (token JWT)
     * @return 201 CREATED
     */
    @PostMapping("/create")
    public ResponseEntity<?> createConfigHour(@RequestBody UserConfigDTO dto,
            @AuthenticationPrincipal UserModel user) {
        return ResponseEntity.status(201).body(service.saveConfig(dto, user));

    }

    /**
     * Rota para busca a configuração atual de agendamento do usuário
     * @param authentication Objeto de autenticação contendo o UserModel (token JWT)
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getConfig(@AuthenticationPrincipal UserModel user) {
        return ResponseEntity.ok(service.findByUserId(user.getId()));
    }

    /**
     * Rota para atualizar o horário de configuração para receber as notifiações
     * @param dto
     * @param user
     * @return
     */
    @PutMapping("/update-time")
    public ResponseEntity<String> updateTime(@RequestBody UserConfigDTO dto, @AuthenticationPrincipal UserModel user) {

        return ResponseEntity.ok(service.updateNotificationTime(dto, user));

    }
}