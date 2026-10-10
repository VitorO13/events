package com.ueg.eventplataform.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.ueg.eventplataform.domain.event.Event;
import com.ueg.eventplataform.domain.event.EventDTO;
import com.ueg.eventplataform.repositories.EventRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;

    }

    private Event toEntity(EventDTO data) {
        Event event = new Event();
        event.setName(data.name());
        event.setHour(data.hour());
        event.setDuration(data.duration());
        event.setDate(data.date());
        event.setDescription(data.description());
        event.setCapacity(data.capacity());
        return event;
    }

    public Event createEvent(EventDTO data) {
        if (this.eventRepository.findByName(data.name()) != null) {
            throw new IllegalArgumentException("Já existe um evento cadastrado com este nome.");
        }

        Event newEvent = new Event();
        newEvent.setName(data.name());
        newEvent.setHour(data.hour());
        newEvent.setDuration(data.duration());
        newEvent.setDate(data.date());
        newEvent.setDescription(data.description());
        newEvent.setCapacity(data.capacity());

        return this.eventRepository.save(newEvent);
    }

    public List<Event> listEvents(String Filter) {
        if (Filter != null && !Filter.trim().isEmpty()) {
            return this.eventRepository.findByNameContainingIgnoreCase(Filter);
        }
        return this.eventRepository.findAll();
    }

    public Event searchById(UUID id) {
        return this.eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado com o ID: " + id));
    }

    public Event updateEvent(UUID id, EventDTO data) {
        Event existentEvent = searchById(id);

        existentEvent.setName(data.name());
        existentEvent.setHour(data.hour());
        existentEvent.setDuration(data.duration());
        existentEvent.setDate(data.date());
        existentEvent.setDescription(data.description());
        existentEvent.setCapacity(data.capacity());

        return this.eventRepository.save(existentEvent);
    }

    public void deleteEvent(UUID id) {
        Event existentEvent = searchById(id);
        this.eventRepository.delete(existentEvent);
    }
}