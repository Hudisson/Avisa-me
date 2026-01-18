package com.hudissonxavier.avisame.model.agenda;

import java.sql.Types;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

import com.hudissonxavier.avisame.model.users.UserModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "tb_daily_tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyTaskModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "task_id")
    private UUID id;

    // Relacionamento com a classe Model UserModel
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @Column(name = "task_title", columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(name = "task_description", columnDefinition = "TEXT", nullable = false)
    private String description;

    // Novo campo: Armazena o dia da semana (ex: MONDAY, TUESDAY...)
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    // Data e hora de quando foi criado a tarefa
    @Column(name = "task_created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime taskCreatedAt;

    // Data e hora de quando foi atualizado
    @Column(name = "task_updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime taskUpdatedAt;


    

    

}
