package com.hudissonxavier.avisame.repository.agenda;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hudissonxavier.avisame.model.agenda.ScheduledEventModel;

@Repository
public interface ScheduledEventRepository extends JpaRepository<ScheduledEventModel, UUID> {

    /**
     * Busca eventos de um usuário específico para uma data determinada.
     * Garante que um usuário não veja eventos de outros, mesmo que na mesma data.
     */
    List<ScheduledEventModel> findByUserIdAndEventDate(UUID userId, LocalDate eventDate);

    /**
     * Busca todos os eventos de um usuário específico (geral).
     */
    List<ScheduledEventModel> findByUserId(UUID userId);

    List<ScheduledEventModel> findByUserIdAndEventDateAndIsNotifiedFalse(UUID userId, LocalDate date);
}
