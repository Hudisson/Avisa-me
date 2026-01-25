package com.hudissonxavier.avisame.controller.agenda;

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

import com.hudissonxavier.avisame.dto.agenda.DailyTaskDTO;
import com.hudissonxavier.avisame.model.users.UserModel;
import com.hudissonxavier.avisame.service.agenda.DailyTaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class DailyTaskController {

    private final DailyTaskService dailyTaskService;

    /**
     * Cria uma nova tarefa diária para o usuário autenticado.
     * @param dto            Dados da tarefa (descrição e status).
     * @param authentication Objeto de autenticação contendo o UserModel.
     * @return 201 Created com os dados da tarefa
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DailyTaskDTO dto, Authentication authentication) {

        UserModel user = (UserModel) authentication.getPrincipal();
        dailyTaskService.create(dto, user);
        HashMap<String, String> response = new HashMap<>();
        response.put("sucesso", "Tarefa criada com exito");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Lista todos os afazeres ativos do usuário logado.
     * @param authentication Objeto de autenticação contendo o UserModel.
     * @return 200 Lista de DTOs das tarefas.
     */
    @GetMapping("/my-tasks")
    public ResponseEntity<List<DailyTaskDTO>> listMyTasks(Authentication authentication) {
        UserModel user = (UserModel) authentication.getPrincipal();
        return ResponseEntity.ok(dailyTaskService.listAllFromUser(user.getId()));
    }

    /**
     * Rota para editar uma tarefa
     * @param id
     * @param taskDto
     * @param currentUser
     * @return // Retorna o DTO com status 200 OK
     */
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody DailyTaskDTO taskDto,
            @AuthenticationPrincipal UserModel currentUser // Injeta o usuário logado automaticamente
    ) {
        // Chama o serviço passando o DTO e o usuário autenticado
        dailyTaskService.update(id, taskDto, currentUser);

        // Definie a mensagem de retorno
        HashMap<String, String> response = new HashMap<>();
        response.put("sucesso", "Tarefa editada com sucesso");

        return ResponseEntity.ok(response);
    }

    /**
     * Remove uma tarefa específica, validando se pertence ao usuário logado.
     * @param id UUID da tarefa a ser removida.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {
        UserModel user = (UserModel) authentication.getPrincipal();
        dailyTaskService.delete(id, user.getId());
        return ResponseEntity.noContent().build();
    }

}
