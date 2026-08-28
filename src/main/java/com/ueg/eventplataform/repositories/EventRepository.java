package com.ueg.eventplataform.repositories;

import com.ueg.eventplataform.domain.event.Event;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<Event, UUID> {
    
   Event findByName(String name);


}
