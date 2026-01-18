package com.hudissonxavier.avisame.service.agenda;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hudissonxavier.avisame.dto.agenda.ScheduledEventDTO;
import com.hudissonxavier.avisame.model.agenda.ScheduledEventModel;
import com.hudissonxavier.avisame.model.users.UserModel;
import com.hudissonxavier.avisame.repository.agenda.ScheduledEventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduledEventService {
    
    private final ScheduledEventRepository repository;

    // Método service para criar um evento
    public ScheduledEventDTO create(ScheduledEventDTO dto, UserModel user) {
        ScheduledEventModel model = dto.toModel(user);
        repository.save(model);
        return ScheduledEventDTO.fromModel(model);
    }


    // Método service para listar eventos da data atual
    public List<ScheduledEventDTO> listTodayEvents(UUID userId) {
        return repository.findByUserIdAndEventDate(userId, LocalDate.now())
                .stream()
                .map(ScheduledEventDTO::fromModel)
                .collect(Collectors.toList());
    }

    // Método service para listar todos os eventos do usuário logado
    public List<ScheduledEventDTO> listAllFromUser(UUID userId){
        return repository.findByUserId(userId)
            .stream()
            .map(ScheduledEventDTO::fromModel)
            .collect(Collectors.toList());
    }

    // Método service para editar um evento
    public ScheduledEventDTO update(UUID enventID, ScheduledEventDTO dto, UserModel user ){
        // Buscar eventos existentes
        ScheduledEventModel event = repository.findById(enventID)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));

        if(user == null || !event.getUser().getId().toString().equals(user.getId().toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não é o dono deste evento");
        }  

        // Atualizar os campos permitidos
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setIsNotified(false); // defini como falso se o evento já tenha sido notificado.  (false -> ainda vai enviar, true -> já foi enviado)

        repository.save(event);

        return ScheduledEventDTO.fromModel(event);
    }

    // Método service para deletar um evento
    public void delete(UUID eventId, UUID userId) {
        ScheduledEventModel event = repository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));

        if (!event.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado a este evento");
        }

        repository.delete(event);
    }
}

