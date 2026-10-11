package com.hudissonxavier.avisame.repository.agenda;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


import com.hudissonxavier.avisame.model.agenda.UserConfigModel;

public interface UserConfigRepository extends JpaRepository<UserConfigModel, UUID>{
    
    /**
     * Busca a configuração de agendamento pertencente ao usuário logado.
     */
    Optional<UserConfigModel> findByUserId(UUID userId);

    // Novo método para o Scheduler encontrar quem notificara agora
    List<UserConfigModel> findByPreferredHour(LocalTime preferredHour);

    //UserConfigModel findByUser(UserModel user);
}