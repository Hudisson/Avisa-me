package com.hudissonxavier.avisame.repository.agenda;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hudissonxavier.avisame.model.agenda.DailyTaskModel;

@Repository
public interface DailyTaskRepository extends JpaRepository<DailyTaskModel, UUID> {
    
    /**
     * Busca todos os afazeres de um usuário específico que estão ativos.
     * O Spring Data JPA filtra automaticamente pelo ID do usuário vinculado.
     */
    List<DailyTaskModel> findByUserIdAndIsActiveTrue(UUID userId);


    // Busca tarefas do usuário filtrando por dia da semana e se está ativa
    List<DailyTaskModel> findByUserIdAndDayOfWeekAndIsActiveTrue(UUID userId, DayOfWeek dayOfWeek);
    
    // Busca todas as tarefas do usuário (independente do dia)
    List<DailyTaskModel> findByUserId(UUID userId);
}
