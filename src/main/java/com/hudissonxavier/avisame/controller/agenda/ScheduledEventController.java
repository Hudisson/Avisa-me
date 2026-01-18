package com.hudissonxavier.avisame.controller.agenda;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class ScheduledEventController {

    private final ScheduledEventService service;

   /**
    * Rota para registrar um novo evento para o usuário autenticado
    * @param dto            Dados do evento a ser registrado no banco de dados
    * @param authentication Objeto de autenticação contendo o UserModel (token JWT)
    * @return 201 Created com os dados do evento
    */
    @PostMapping("/create")
    public ResponseEntity<ScheduledEventDTO> create(@RequestBody ScheduledEventDTO dto, Authentication authentication) {
        UserModel user = (UserModel) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto, user));
        //return ResponseEntity.ok(service.create(dto, user));
    }

    /**
     *  Rota que retorna os eventos agendados do usuário logado para a data atual
     * @param authentication
     * @return 200 e a lista de DTOs dos eventos ou uma resposta customizada se não hover eventos
     */
    @GetMapping("/today")
    public ResponseEntity<?> listToday(Authentication authentication) {

        UserModel user = (UserModel) authentication.getPrincipal();

        // Busca a lista de eventos da data atual
        List<ScheduledEventDTO> events = service.listTodayEvents(user.getId());

        // Retorna uma resposta customizada se a lista de eventos estiver vazia
        if (events.isEmpty()) {

            HashMap<String, String> response = new HashMap<>();
            response.put("message", "Você não tem eventos agendado para hoje");
            response.put("date", LocalDate.now().toString());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(events);
    }

    /**
     * Rota para listar todos os eventos do usuário
     * @param authentication
     * @return 200 e lista de todos os eventos
     */
    @GetMapping("/list")
    public ResponseEntity<?> listAllEvents(Authentication authentication){

        UserModel user = (UserModel) authentication.getPrincipal();

        // Buscar lista com todos eventos
        List<ScheduledEventDTO> events = service.listAllFromUser(user.getId());

        // Retorna uma resposta customizada se a lista de eventos estiver vazia
        if (events.isEmpty()) {

            HashMap<String, String> response = new HashMap<>();
            response.put("message", "Você não tem eventos agendados");
        
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.ok(events);

    }

    /**
     * Rota para editar um evento
     * @param id
     * @param eventDto
     * @param currentUser
     * @return 200 e os dados do evento que foi editado
     */
    @PutMapping("/edit/{id}")
    public ResponseEntity<ScheduledEventDTO> update(
        @PathVariable UUID id,
        @RequestBody ScheduledEventDTO eventDto,
        @AuthenticationPrincipal UserModel currentUser // Injeta o usuário logado automaticamente
    ){

        // Chama o serviço passando o DTO e o usuário autenticado
        ScheduledEventDTO updatedEvent = service.update(id, eventDto, currentUser);

        // Retorna o DTO com status 200 OK
        return ResponseEntity.ok(updatedEvent);

    }

   /**
    * Rota para Excluir um evento
    * @param id
    * @param authentication
    * @return
    */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {
        UserModel user = (UserModel) authentication.getPrincipal();
        service.delete(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
