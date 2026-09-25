package com.ueg.eventplataform.mappers;

import com.ueg.eventplataform.domain.event.Event;
import com.ueg.eventplataform.domain.event.EventDTO;

@Mapper(componentModel = "spring")
public interface EventMapper {

   public default Event toEntity(EventDTO dto) {
        return new Event(
                null,
                dto.name(),
                dto.hour(),
                dto.duration(),
                dto.date(),
                dto.description(),
                dto.capacity()
        );
    }

    public default EventDTO toDTO(Event event) {
        return new EventDTO(
                null, 
                event.getName(),
                event.getHour(),
                event.getDuration(),
                event.getDate(),
                event.getDescription(),
                event.getCapacity()
        );
    }

}
