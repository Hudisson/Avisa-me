package com.hudissonxavier.avisame.service.notification;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hudissonxavier.avisame.model.agenda.DailyTaskModel;
import com.hudissonxavier.avisame.model.agenda.ScheduledEventModel;
import com.hudissonxavier.avisame.model.users.UserModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    
    private final EmailService emailService;

    public void processarEnvio(UserModel user, List<DailyTaskModel> tarefas, List<ScheduledEventModel> eventos) {
        String subject = "🔔 AvisaMe: Seu resumo de hoje";
        String body = buildHtmlBody(user.getName(), tarefas, eventos);

        emailService.sendEmail(user.getEmail(), subject, body);
    }

    private String buildHtmlBody(String nome, List<DailyTaskModel> tarefas, List<ScheduledEventModel> eventos) {
        StringBuilder html = new StringBuilder();
        html.append("<h1>Olá, ").append(nome).append("!</h1>");
        html.append("<p>Aqui está o que tens planejado para hoje:</p>");

        if (!tarefas.isEmpty()) {
            html.append("<h3>📌 Afazeres do Dia:</h3><ul>");
            tarefas.forEach(t -> html.append("<li>").append(t.getDescription()).append("</li>"));
            html.append("</ul>");
        }

        if (!eventos.isEmpty()) {
            html.append("<h3>📅 Eventos Agendados:</h3><ul>");
            eventos.forEach(e -> html.append("<li><strong>")
                .append(e.getTitle()).append("</strong>: ")
                .append(e.getDescription()).append("</li>"));
            html.append("</ul>");
        }

        html.append("<br><p>Tenha um excelente dia!<br>Equipa <b>AvisaMe</b></p>");
        return html.toString();
    }
}
