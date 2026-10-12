package com.hudissonxavier.avisame.service.notification;

import java.util.List;

import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import com.hudissonxavier.avisame.model.agenda.DailyTaskModel;
import com.hudissonxavier.avisame.model.agenda.ScheduledEventModel;
import com.hudissonxavier.avisame.model.users.UserModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

    // Lista de tags HTML permitidas no conteúdo do e-mail
    private static final Safelist EMAIL_CONTENT_SAFELIST = new Safelist()
            .addTags("p", "br", "h1", "h2", "h3", "h4", "h5", "h6",
                    "ul", "ol", "li", "strong", "b", "em", "i", "u", "blockquote");
    
    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    // Processa o envio de notificações
    public void processarEnvio(UserModel user, List<DailyTaskModel> tarefas, List<ScheduledEventModel> eventos) {
        String subject = "🔔 AvisaMe: Seu resumo de hoje";
        String body = buildHtmlBody(user.getName(), tarefas, eventos);

        emailService.sendEmail(user.getEmail(), subject, body);
    }

    // Constrói o corpo do e-mail em HTML
    private String buildHtmlBody(String nome, List<DailyTaskModel> tarefas, List<ScheduledEventModel> eventos) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang=\"pt-BR\"><head>")
                .append("<meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("<title>Seu resumo de hoje</title></head>")
                .append("<body style=\"margin:0;padding:0;background-color:#f4f4f5;font-family:Arial,Helvetica,sans-serif;color:#18181b;\">")
                .append("<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:#f4f4f5;padding:40px 20px;\">")
                .append("<tr><td align=\"center\">")
                .append("<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"max-width:560px;background-color:#ffffff;border-radius:12px;overflow:hidden;\">")
                .append("<tr><td align=\"center\" style=\"background-color:#18181b;padding:32px 24px;\">")
                .append("<h1 style=\"margin:0;color:#ffffff;font-size:24px;line-height:1.3;font-weight:700;\">AvisaMe: Seu resumo de hoje</h1>")
                .append("</td></tr>")
                .append("<tr><td style=\"padding:36px 32px;\">")
                .append("<h2 style=\"margin:0 0 18px;font-size:22px;line-height:1.4;color:#18181b;\">Olá, ")
                .append(escapeHtml(nome))
                .append("!</h2>")
                .append("<p style=\"margin:0 0 24px;font-size:16px;line-height:1.7;color:#52525b;\">Aqui está o que você planejou para hoje:</p>");

        if (!tarefas.isEmpty()) {
            html.append("<h3 style=\"margin:0 0 12px;font-size:18px;line-height:1.4;color:#18181b;\">Afazeres do dia</h3>");
            html.append("<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin:0 0 24px;\">");
            tarefas.forEach(t -> html.append("<tr><td style=\"padding:14px 16px;background-color:#f4f4f5;border:1px solid #e4e4e7;border-radius:8px;font-size:15px;line-height:1.6;color:#52525b;\">")
                    .append(sanitizeHtml(t.getDescription()))
                    .append("</td></tr><tr><td height=\"8\" style=\"font-size:0;line-height:0;\">&nbsp;</td></tr>"));
            html.append("</table>");
        }

        if (!eventos.isEmpty()) {
            html.append("<h3 style=\"margin:0 0 12px;font-size:18px;line-height:1.4;color:#18181b;\">Eventos agendados Para Hoje</h3>");
            html.append("<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin:0 0 24px;\">");
            eventos.forEach(e -> html.append("<tr><td style=\"padding:14px 16px;background-color:#f4f4f5;border:1px solid #e4e4e7;border-radius:8px;font-size:15px;line-height:1.6;color:#52525b;\">")
                    .append("<strong style=\"color:#18181b;\">").append(escapeHtml(e.getTitle())).append("</strong><br>")
                    .append(sanitizeHtml(e.getDescription()))
                    .append("</td></tr><tr><td height=\"8\" style=\"font-size:0;line-height:0;\">&nbsp;</td></tr>"));
            html.append("</table>");
        }

        html.append("<p style=\"margin:0;font-size:15px;line-height:1.7;color:#52525b;\">Tenha um excelente dia!<br>Equipe <strong style=\"color:#18181b;\">AvisaMe</strong></p>")
                .append("</td></tr>")
                .append("<tr><td align=\"center\" style=\"padding:22px 32px;background-color:#fafafa;border-top:1px solid #e4e4e7;\">")
                .append("<p style=\"margin:0;font-size:12px;line-height:1.5;color:#71717a;\">Este é um e-mail automático. Por favor, não responda a esta mensagem.</p>")
                .append("</td></tr></table></td></tr></table></body></html>");
        return html.toString();
    }

    /**
     * Escapa caracteres HTML para evitar injeção de código.
     *
     * @param value O valor a ser escapado.
     * @return O valor escapado.
     */
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    /**
     * Limpa o conteúdo HTML para evitar injeção de código.
     *
     * @param value O valor a ser limpo.
     * @return O valor limpo.
     */
    private String sanitizeHtml(String value) {
        return value == null ? "" : Jsoup.clean(value, EMAIL_CONTENT_SAFELIST);
    }
}