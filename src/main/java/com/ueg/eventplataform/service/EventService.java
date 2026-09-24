package com.ueg.eventplataform.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.ueg.eventplataform.domain.event.Event;
import com.ueg.eventplataform.domain.event.EventDTO;
import com.ueg.eventplataform.mappers.EventMapper;
import com.ueg.eventplataform.repositories.EventRepository;

@Service 
public class EventService {
    
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    public Event criarEvento(EventDTO data) {
        if (this.eventRepository.findByName(data.name()) != null) {
            throw new IllegalArgumentException("Já existe um evento cadastrado com este nome.");
        }
        
        Event newEvent = this.eventMapper.toEntity(data);
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