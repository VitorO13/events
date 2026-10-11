package com.ueg.eventplataform.repositories;

import com.ueg.eventplataform.domain.registration.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface RegistrationRepository extends JpaRepository<Registration, UUID> {
    long countByEventId(UUID eventId);

    java.util.List<com.ueg.eventplataform.domain.registration.Registration> findByUserId(UUID userId);

    boolean existsByUserIdAndEventId(UUID userId, UUID eventId);

    boolean existsByUserId(UUID userId);

    @Modifying
    @Query("update registrations r set r.isPresent = true where r.id = :id and r.isPresent = false")
    int checkInIfAbsent(@Param("id") UUID id);
}
