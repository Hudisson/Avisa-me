package com.hudissonxavier.avisame.service.agenda;

import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hudissonxavier.avisame.dto.agenda.UserConfigDTO;
import com.hudissonxavier.avisame.model.agenda.UserConfigModel;
import com.hudissonxavier.avisame.model.users.UserModel;
import com.hudissonxavier.avisame.repository.agenda.UserConfigRepository;
import com.hudissonxavier.avisame.service.ServiceResponse;

import jakarta.transaction.Transactional;

/**
 * Serviço para gerenciar as preferências de horário do usuário.
 */

@Service
public class UserConfigService {
    
    private final UserConfigRepository repository;

    public UserConfigService(UserConfigRepository repository) {
        this.repository = repository;
    }

    public Map<String, String> saveConfig(UserConfigDTO dto, UserModel user) {
        // Busca se já existe uma configuração para este usuário, senão cria uma nova através do DTO
        UserConfigModel config = repository.findByUserId(user.getId())
                .orElseGet(() -> dto.toModel(user));

        // Se já existia, apenas atualizamos os campos necessários
        if (config.getId() != null) {
            config.setPreferredHour(dto.getPreferredHour());
            // Lógica interna do DTO para gerar cron (pode ser chamada aqui ou no DTO)
            String newCron = String.format("0 %d %d * * *", 
                dto.getPreferredHour().getMinute(), 
                dto.getPreferredHour().getHour());
            config.setCronExpression(newCron);
        }

        repository.save(config);
        return ServiceResponse.message("sucesso", "Horário para receber as notificações definido com sucesso");
    }

    public Object findByUserId(UUID userId) {
        return repository.findByUserId(userId)
                .<Object>map(UserConfigDTO::fromModel)
                .orElseGet(() -> Map.of(
                        "message", "Você ainda não configurou um horário para receber as notificações!",
                        "status", "405"));
    }

    @Transactional
    public String updateNotificationTime(UserConfigDTO dto, UserModel user){
        // Busca pela relação com o Usuário (findByUserId)
        UserConfigModel config = repository.findByUserId(user.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
            "Configuração não encontrada para este usuário. Crie uma primeiro."));


        // Converte String "08:30" para LocalTime
        LocalTime time = dto.getPreferredHour();
        config.setPreferredHour(time);

        // Gera a Cron Expression: "0 minuto hora * * *"
        // Exemplo: 08:30 vira "0 30 8 * * *"
        String cron = String.format("0 %d %d * * *", time.getMinute(), time.getHour());
        config.setCronExpression(cron);

        repository.save(config);
        return "Horário de notificação atualizado com sucesso!";
    }
}