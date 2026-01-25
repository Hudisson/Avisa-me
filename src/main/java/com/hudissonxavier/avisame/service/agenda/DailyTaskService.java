package com.hudissonxavier.avisame.service.agenda;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hudissonxavier.avisame.dto.agenda.DailyTaskDTO;
import com.hudissonxavier.avisame.model.agenda.DailyTaskModel;
import com.hudissonxavier.avisame.model.users.UserModel;
import com.hudissonxavier.avisame.repository.agenda.DailyTaskRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Serviço para gerenciamento de afazeres diários.
 * Garante que o usuário acesse apenas suas próprias tarefas.
 */

@Service
@RequiredArgsConstructor
public class DailyTaskService {

    private final DailyTaskRepository repository;

    // Método service para criar uma tarefa
    public DailyTaskDTO create(DailyTaskDTO dto, UserModel user) {
        DailyTaskModel model = dto.toModel(user);
        repository.save(model);
        return DailyTaskDTO.fromModel(model);
    }

    // Método service para listar as tarefas do usuário logado por dia da semana
    public List<DailyTaskDTO> listAllFromUser(UUID userId) {
        return repository.findAllByUserIdOrderByDayOfWeek(userId)
                .stream()
                .map(DailyTaskDTO::fromModel)
                .collect(Collectors.toList());
    }

    // Método service para deletar uma tarefa
    public void delete(UUID taskId, UUID userId) {
        DailyTaskModel task = repository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        // Validação de segurança: a tarefa pertence ao usuário logado?
        if (!task.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado a esta tarefa");
        }

        repository.delete(task);
    }

     // Método service para atualizar/editar uma tarefa
    @Transactional
    public DailyTaskDTO update(UUID taskId, DailyTaskDTO dto, UserModel user) {
        // Busca a tarefa existente
        DailyTaskModel task = repository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        if (user == null || !task.getUser().getId().toString().equals(user.getId().toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não é o dono desta tarefa");
        }

        // Atualiza os campos permitidos
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDayOfWeek(dto.getDayOfWeek());

        // Trata o isActive: se vier nulo no DTO, mantém o valor atual ou define como
        // true
        if (dto.getIsActive() != null) {
            task.setIsActive(dto.getIsActive());
        }

        // Salva as alterações
        repository.save(task);

        // Retorna o DTO atualizado
        return DailyTaskDTO.fromModel(task);
    }

}
