package com.hudissonxavier.avisame.model.agenda;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

import com.hudissonxavier.avisame.model.users.UserModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_scheduled_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduledEventModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "event_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @Column(name = "event_title", length = 150, nullable = false)
    private String title;

    @Column(name = "event_description", columnDefinition = "TEXT")
    private String description;

    // Data de quando o evento será realizado
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Builder.Default
    @Column(name = "is_notified")
    private Boolean isNotified = false;

    // Data e hora de quando foi criado o evento
    @Column(name = "event_created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime eventCreatedAt;

    // Data e hora de quando foi atualizado
    @Column(name = "event_updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime eventUpdatedAt;

}
