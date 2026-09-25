package com.ueg.eventplataform.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ueg.eventplataform.domain.event.Event;
import com.ueg.eventplataform.domain.event.EventDTO;
import com.ueg.eventplataform.mappers.EventMapper;
import com.ueg.eventplataform.repositories.EventRepository;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    @Test
    void naoDevePermitirCriarEventoComNomeDuplicado() {
        EventDTO dadosDoEvento = new EventDTO(null, "Semana da Computação", "14:00", (byte) 2, null, "Descrição", 100);
        Event eventoExistente = new Event();

        Mockito.when(eventRepository.findByName("Semana da Computação")).thenReturn(eventoExistente);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            eventService.createEvent(dadosDoEvento);
        });
    }

}
