package com.hudissonxavier.avisame.model.agenda;

import java.sql.Types;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

import com.hudissonxavier.avisame.model.users.UserModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user_configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserConfigModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "config_id")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserModel user;

    @Column(name = "preferred_hour")
    private LocalTime preferredHour;

    @Column(name = "cron_expression", length = 50)
    private String cronExpression;

    // Data e hora de quando foi criado a configuração de horário
    @Column(name = "config_created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime configCreatedAt;

    // Data e hora de quando foi atualizado
    @Column(name = "config_updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime configUpdatedAt;
}
