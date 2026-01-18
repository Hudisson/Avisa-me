package com.hudissonxavier.avisame.dto.agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.hudissonxavier.avisame.model.agenda.ScheduledEventModel;
import com.hudissonxavier.avisame.model.users.UserModel;

import lombok.Data;

/**
 * DTO para agendamentos por data específica (Modo 2).
 */

@Data
public class ScheduledEventDTO {
    
    private UUID id;
    private String title;
    private String description;
    private LocalDate eventDate;
    private Boolean isNotified;
    private LocalDateTime eventCreatedAt;
    private LocalDateTime eventUpdatedAt;


    public ScheduledEventModel toModel(UserModel user) {
        return ScheduledEventModel.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .eventDate(this.eventDate)
                .isNotified(this.isNotified)
                .eventCreatedAt(this.eventCreatedAt)
                .eventUpdatedAt(this.eventUpdatedAt)
                .user(user)
                .build();
    }

    public static ScheduledEventDTO fromModel(ScheduledEventModel model) {
        ScheduledEventDTO dto = new ScheduledEventDTO();
        dto.setId(model.getId());
        dto.setTitle(model.getTitle());
        dto.setDescription(model.getDescription());
        dto.setEventDate(model.getEventDate());
        dto.setIsNotified(model.getIsNotified());
        dto.setEventCreatedAt(model.getEventCreatedAt());
        dto.setEventUpdatedAt(model.getEventUpdatedAt());
        return dto;
    }
}
