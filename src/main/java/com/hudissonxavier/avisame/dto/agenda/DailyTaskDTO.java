package com.hudissonxavier.avisame.dto.agenda;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.UUID;

import com.hudissonxavier.avisame.model.agenda.DailyTaskModel;
import com.hudissonxavier.avisame.model.users.UserModel;

import lombok.Data;

/**
 * DTO para captura e resposta de afazeres diários (Modo 1).
 */

@Data
public class DailyTaskDTO {

    private UUID id;
    private String title;
    private String description;
    private Boolean isActive;

    private DayOfWeek dayOfWeek; // Ex: MONDAY, TUESDAY...
    private LocalDateTime taskCreatedAt;
    private LocalDateTime taskUpdatedAt;

    // Converte para a model vinculando ao usuário logado
    public DailyTaskModel toModel(UserModel user) {
        return DailyTaskModel.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .dayOfWeek(this.dayOfWeek)
                .taskCreatedAt(this.taskCreatedAt)
                .taskUpdatedAt(this.taskUpdatedAt)
                .isActive(this.isActive)
                .user(user)
                .build();
    }

    // Cria o DTO a partir da model
    public static DailyTaskDTO fromModel(DailyTaskModel model) {
        DailyTaskDTO dto = new DailyTaskDTO();
        dto.setId(model.getId());
        dto.setTitle(model.getTitle());
        dto.setDescription(model.getDescription());
        dto.setDayOfWeek(model.getDayOfWeek());
        dto.setIsActive(model.getIsActive());
        dto.setDayOfWeek(model.getDayOfWeek());
        dto.setTaskCreatedAt(model.getTaskCreatedAt());
        dto.setTaskUpdatedAt(model.getTaskUpdatedAt());
        return dto;
    }

}
