package com.ueg.eventplataform.service;

import com.ueg.eventplataform.domain.event.Event;
import com.ueg.eventplataform.domain.registration.Registration;
import com.ueg.eventplataform.domain.users.User;
import com.ueg.eventplataform.repositories.EventRepository;
import com.ueg.eventplataform.repositories.RegistrationRepository;
import com.ueg.eventplataform.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public UserService(UserRepository userRepository, EventRepository eventRepository,
            RegistrationRepository registrationRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public void deleteUser(UUID id) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null && existingUser.getRole() == com.ueg.eventplataform.domain.users.UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Administradores são mantidos manualmente no banco de dados.");
        }
        if (!this.userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }
        if (this.registrationRepository.existsByUserId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O usuário possui inscrições e não pode ser excluído.");
        }
        this.userRepository.deleteById(id);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Registration registerUserToEvent(UUID userId, UUID eventId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        // Serializa as inscrições no mesmo evento até o fim da transação.
        Event event = this.eventRepository.findByIdForUpdate(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));
        if (this.registrationRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já inscrito neste evento.");
        }

        long currentRegistrations = this.registrationRepository.countByEventId(eventId);
        if (currentRegistrations >= event.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O evento já atingiu a lotação máxima.");
        }
        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegistrationDate(LocalDateTime.now());

        return this.registrationRepository.save(registration);
    }
}
