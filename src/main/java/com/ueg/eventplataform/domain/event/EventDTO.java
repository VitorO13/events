package com.ueg.eventplataform.domain.event;

public record EventDTO(String name, String hour, byte duration, java.sql.Date date, String description, int capacity) {

}
