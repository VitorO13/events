package com.ueg.eventplataform.service;

import com.ueg.eventplataform.domain.event.Event;
import com.ueg.eventplataform.domain.registration.Registration;
import com.ueg.eventplataform.domain.users.User;
import com.ueg.eventplataform.repositories.EventRepository;
import com.ueg.eventplataform.repositories.RegistrationRepository;
import com.ueg.eventplataform.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public UserService(UserRepository userRepository, EventRepository eventRepository, RegistrationRepository registrationRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

   
    public void deleteUser(UUID id) {
        if (!this.userRepository.existsById(id)) {
            throw new RuntimeException("Utilizador não encontrado.");
        }
        this.userRepository.deleteById(id);
    }


    public void registerUserToEvent(UUID userId, UUID eventId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado."));
        
        Event event = this.eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado."));

        long currentRegistrations = this.registrationRepository.countByEventId(eventId);
        if (currentRegistrations >= event.getCapacity()) {
            throw new RuntimeException("Atenção: O evento já atingiu a lotação máxima.");
        }
        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegistrationDate(LocalDateTime.now());

        this.registrationRepository.save(registration);
    }
}

