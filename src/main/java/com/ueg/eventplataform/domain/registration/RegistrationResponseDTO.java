package com.ueg.eventplataform.domain.registration;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegistrationResponseDTO(UUID id, UUID userId, UUID eventId,
        LocalDateTime registrationDate, boolean present) {
    public static RegistrationResponseDTO from(Registration registration) {
        return new RegistrationResponseDTO(registration.getId(), registration.getUser().getId(),
                registration.getEvent().getId(), registration.getRegistrationDate(), registration.isPresent());
    }
}
