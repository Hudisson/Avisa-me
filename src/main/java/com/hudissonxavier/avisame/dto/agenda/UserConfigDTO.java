package com.hudissonxavier.avisame.dto.agenda;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.hudissonxavier.avisame.model.agenda.UserConfigModel;
import com.hudissonxavier.avisame.model.users.UserModel;

import lombok.Data;

/**
 * DTO para configurar o horário preferencial de recebimento do e-mail.
 */

@Data
public class UserConfigDTO {

    private UUID id;
    private UserModel user;
    private LocalTime preferredHour;
    private String cronExpression;
    private LocalDateTime configCreatedAt;
    private LocalDateTime configUpdatedAt;

    public UserConfigModel toModel(UserModel user) {
        return UserConfigModel.builder()
                .user(user)
                .preferredHour(this.preferredHour) //
                .cronExpression(generateCron())
                .user(this.user)
                .configCreatedAt(this.configCreatedAt) //
                .configUpdatedAt(this.configUpdatedAt) //
                .build();
    }

    public static UserConfigDTO fromModel(UserConfigModel model) {
        UserConfigDTO dto = new UserConfigDTO();
        dto.setPreferredHour(model.getPreferredHour()); //
        dto.setConfigCreatedAt(model.getConfigCreatedAt());//
        dto.setConfigUpdatedAt(model.getConfigUpdatedAt()); //
        dto.setId(model.getId());
        dto.setCronExpression(model.getCronExpression());
        return dto;
    }

    // Gera o formato: "0 minuto hora * * *"
    private String generateCron() {
        return String.format("0 %d %d * * *", 
                this.preferredHour.getMinute(), 
                this.preferredHour.getHour());
    }
}
