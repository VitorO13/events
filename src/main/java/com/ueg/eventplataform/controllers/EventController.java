package com.ueg.eventplataform.controllers;

import com.ueg.eventplataform.repositories.UserRepository;
import org.springframework.web.bind.annotation.RestController;

import com.ueg.eventplataform.domain.event.Event;
import com.ueg.eventplataform.domain.event.EventDTO;
import com.ueg.eventplataform.repositories.EventRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/events")

public class EventController {

    private final EventRepository eventRepository;


    EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping("/create-events")
    public ResponseEntity createEvent(@RequestBody @Valid EventDTO data) {
        Event newEvent = new Event(null, data.name(), data.hour(), data.duration(), data.date(), data.description(), data.capacity());
        this.eventRepository.save(newEvent);
        return ResponseEntity.status(201).build();
     }

    @GetMapping("/list-events")
    public String getMethodName(@RequestParam String param) {
        if(eventRepository .findByName(param) != null) {
            return eventRepository.findByName(param).toString();
        }
        return new String();
    
    

    }
}
  