package com.hudissonxavier.avisame.agendador;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hudissonxavier.avisame.model.agenda.DailyTaskModel;
import com.hudissonxavier.avisame.model.agenda.ScheduledEventModel;
import com.hudissonxavier.avisame.model.agenda.UserConfigModel;
import com.hudissonxavier.avisame.model.users.UserModel;
import com.hudissonxavier.avisame.repository.agenda.DailyTaskRepository;
import com.hudissonxavier.avisame.repository.agenda.ScheduledEventRepository;
import com.hudissonxavier.avisame.repository.agenda.UserConfigRepository;
import com.hudissonxavier.avisame.service.notification.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Componente responsável por monitorar os horários de disparo.
 * Executa uma verificação a cada minuto para processar as filas de envio.
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class RoutineScheduler {

    private final UserConfigRepository configRepository;
    private final DailyTaskRepository dailyTaskRepository;
    private final ScheduledEventRepository eventRepository;
    private final NotificationService notificationService;

    /**
     * Método agendado para rodar a cada minuto (0 segundos de cada minuto).
     * Cron: "segundo minuto hora dia mes dia_da_semana"
     */
    @Scheduled(cron = "0 * * * * *")
    public void executeRoutineCheck() {
        // 1. Obtém o horário atual do sistema truncado (ex: 08:30:00)
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        log.info("Iniciando verificação de rotina para o horário: {}", now);

        // 2. Busca no banco todos os usuários que querem receber e-mail neste minuto
        List<UserConfigModel> pendingConfigs = configRepository.findByPreferredHour(now);

        if (pendingConfigs.isEmpty()) {
            return;
        }

        log.info("Encontrados {} usuários para processar.", pendingConfigs.size());

        for (UserConfigModel config : pendingConfigs) {
            UserModel user = config.getUser();

            try {

                // Busca tarefas recorrentes do dia da semana atual (Modo 1)
                List<DailyTaskModel> dailyTasks = dailyTaskRepository
                        .findByUserIdAndDayOfWeekAndIsActiveTrue(user.getId(), dayOfWeek);

                // Busca eventos específicos agendados para a data de hoje (Modo 2)
                // List<ScheduledEventModel> scheduledEvents = eventRepository
                // .findByUserIdAndEventDate(user.getId(), today);

                // Busca apenas eventos de HOJE que AINDA NÃO foram notificados (Modo 2)
                List<ScheduledEventModel> scheduledEvents = eventRepository
                        .findByUserIdAndEventDateAndIsNotifiedFalse(user.getId(), today);

                // Só dispara o processo de e-mail se houver conteúdo para enviar
                if (!dailyTasks.isEmpty() || !scheduledEvents.isEmpty()) {

                    // Dispara o envio do e-mail
                    notificationService.processarEnvio(user, dailyTasks, scheduledEvents);

                    // Atualiza o status dos eventos para TRUE (1) no banco de dado
                    if (!scheduledEvents.isEmpty()) {
                        scheduledEvents.forEach(event -> event.setIsNotified(true));
                        eventRepository.saveAll(scheduledEvents);

                        log.info("Sucesso: {} eventos marcados como notificados para {}",
                                scheduledEvents.size(), user.getEmail());
                    }

                    log.info("Notificação enviada com sucesso para o usuário: {}", user.getEmail());

                } else {
                    log.info("Usuário {} não possui afazeres para hoje ({}).", user.getEmail(), dayOfWeek);
                }

            } catch (Exception e) {
                log.error("Erro ao processar notificações para o usuário {}: {}", user.getEmail(), e.getMessage());
            }
        }
    }

}
