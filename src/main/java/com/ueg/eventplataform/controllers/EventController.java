package com.ueg.eventplataform.controllers;

import com.ueg.eventplataform.service.EventService;
import org.springframework.web.bind.annotation.RestController;
import com.ueg.eventplataform.domain.event.Event;
import com.ueg.eventplataform.domain.event.EventDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/events")

public class EventController {
private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody @Valid EventDTO data) {
        Event createdEvent = this.eventService.createEvent(data);
        return ResponseEntity.status(201).body(createdEvent);
    }

    @GetMapping
    public ResponseEntity<List<Event>> listEvents(@RequestParam(name = "nome", required = false) String nome) {
        List<Event> eventos = this.eventService.listEvents(nome);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable UUID id) {
        Event evento = this.eventService.searchById(id);
        return ResponseEntity.ok(evento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable UUID id, @RequestBody @Valid EventDTO data) {
        Event updatedEvent = this.eventService.updateEvent(id, data);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        this.eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
  