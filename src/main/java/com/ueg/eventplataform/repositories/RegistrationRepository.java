package com.ueg.eventplataform.repositories;

import com.ueg.eventplataform.domain.registration.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RegistrationRepository extends JpaRepository<Registration, UUID> {
    long countByEventId(UUID eventId);
}


