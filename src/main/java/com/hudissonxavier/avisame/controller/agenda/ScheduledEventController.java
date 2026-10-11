package com.hudissonxavier.avisame.controller.agenda;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hudissonxavier.avisame.dto.agenda.ScheduledEventDTO;
import com.hudissonxavier.avisame.model.users.UserModel;
import com.hudissonxavier.avisame.service.agenda.ScheduledEventService;


@RestController
@RequestMapping("/events")
public class ScheduledEventController {

    private final ScheduledEventService service;

    public ScheduledEventController(ScheduledEventService service) {
        this.service = service;
    }

    /**
     * Rota para registrar um novo evento para o usuário autenticado
     * 
     * @param dto            Dados do evento a ser registrado no banco de dados
     * @param authentication Objeto de autenticação contendo o UserModel (token JWT)
     * @return 201 Created com os dados do evento
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ScheduledEventDTO dto, @AuthenticationPrincipal UserModel user) {
        return ResponseEntity.status(201).body(service.create(dto, user));
    }

    /**
     * Rota que retorna os eventos agendados do usuário logado para a data atual
     * 
     * @param authentication
     * @return 200 e a lista de DTOs dos eventos ou uma resposta customizada se não
     *         hover eventos
     */
    @GetMapping("/today")
    public ResponseEntity<?> listToday(@AuthenticationPrincipal UserModel user) {
        return ResponseEntity.ok(service.listTodayEvents(user.getId()));
    }

    /**
     * Rota para listar todos os eventos do usuário
     * 
     * @param authentication
     * @return 200 e lista de todos os eventos
     */
    @GetMapping("/list")
    public ResponseEntity<?> listAllEvents(@AuthenticationPrincipal UserModel user) {
        return ResponseEntity.ok(service.listAllFromUser(user.getId()));
    }

    /**
     * Busca um evento específico pelo ID, validando se pertence ao usuário logado.
     * 
     * @param id             UUID do evento.
     * @param authentication Objeto de autenticação contendo o UserModel.
     * @return 200 OK com o DTO do evento.
     */
    @GetMapping("/list/{id}")
    public ResponseEntity<ScheduledEventDTO> getById(@PathVariable UUID id, @AuthenticationPrincipal UserModel user) {
        return ResponseEntity.ok(service.getById(id, user.getId()));
    }

    /**
     * Rota para editar um evento
     * 
     * @param id
     * @param eventDto
     * @param currentUser
     * @return 200 e os dados do evento que foi editado
     */
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody ScheduledEventDTO eventDto,
            @AuthenticationPrincipal UserModel currentUser
    ) {
        return ResponseEntity.ok(service.update(id, eventDto, currentUser));

    }

    /**
     * Rota para Excluir um evento
     * 
     * @param id
     * @param authentication
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @AuthenticationPrincipal UserModel user) {
        service.delete(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}