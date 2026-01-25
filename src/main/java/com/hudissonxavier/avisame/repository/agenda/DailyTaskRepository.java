package com.hudissonxavier.avisame.repository.agenda;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hudissonxavier.avisame.model.agenda.DailyTaskModel;

@Repository
public interface DailyTaskRepository extends JpaRepository<DailyTaskModel, UUID> {
    
    /**
     * Busca todas as tarefas com status ativa (true) de um usuário específico.
     * O Spring Data JPA filtra automaticamente pelo ID do usuário vinculado.
     */
    List<DailyTaskModel> findByUserIdAndIsActiveTrue(UUID userId);


    // Busca tarefas do usuário filtrando por dia da semana e se está ativa
    List<DailyTaskModel> findByUserIdAndDayOfWeekAndIsActiveTrue(UUID userId, DayOfWeek dayOfWeek);
    
    // Busca todas as tarefas do usuário (independente do dia)
    List<DailyTaskModel> findByUserId(UUID userId);


    // Busca as tarefas ordenadas po dia da semana
    @Query("SELECT t FROM DailyTaskModel t WHERE t.user.id = :userId " +
       "ORDER BY FIELD(t.dayOfWeek, 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')")
    List<DailyTaskModel> findAllByUserIdOrderByDayOfWeek(@Param("userId") UUID userId);
}
