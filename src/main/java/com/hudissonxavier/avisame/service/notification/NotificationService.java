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

        html.append(
                "<br><hr style='border:none;border-top:1px solid #ddd;margin:20px 0;'>");
        html.append(
                "<p style='font-family:Arial,sans-serif;font-size:13px;color:#555;text-align:center;'>");

        html.append("Tenha um excelente dia!<br>");
        html.append("Equipa <b>AvisaMe</b><br><br>");

        html.append("<span style='color:#888;'>Links úteis:</span><br><br>");

        html.append(
                "<a href='https://github.com/hudisson' style='margin-right:10px;'>");
        html.append(
                "<img src='https://cdn-icons-png.flaticon.com/512/25/25231.png' width='24' height='24' alt='GitHub' style='vertical-align:middle;'>");
        html.append("</a>");

        html.append("<a href='https://linkedin.com/in/hudisson-xavier'>");
        html.append(
                "<img src='https://cdn-icons-png.flaticon.com/512/174/174857.png' width='24' height='24' alt='LinkedIn' style='vertical-align:middle;'>");
        html.append("</a>");

        html.append("</p>");

        return html.toString();
    }
}
