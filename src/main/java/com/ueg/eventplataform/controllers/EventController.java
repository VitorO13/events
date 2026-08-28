package com.ueg.eventplataform.controllers;

import com.ueg.eventplataform.repositories.UserRepository;
import org.springframework.web.bind.annotation.RestController;
import com.ueg.eventplataform.domain.event.EventDTO;
import com.ueg.eventplataform.repositories.EventRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/event")

public class EventController {

    private final EventRepository eventRepository;


    EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    

    @PostMapping("/create")
    public ResponseEntity createEvent(@RequestBody @Valid EventDTO data) {
        if (this.eventRepository.findByName(data.name()) != null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity updateEvent (@RequestBody @Valid EventDTO data){
        if (this.eventRepository.findByName(data.name()) == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
