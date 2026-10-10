package com.ueg.eventplataform.repositories;

import com.ueg.eventplataform.domain.event.Event;

import java.util.List;
import java.util.UUID;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<Event, UUID> {
   @Lock(LockModeType.PESSIMISTIC_WRITE)
   @Query("select e from Event e where e.id = :id")
   Optional<Event> findByIdForUpdate(@Param("id") UUID id);
    
   Event findByName(String name);

   List<Event> findByNameContainingIgnoreCase(String name);

}
