package com.ueg.eventplataform.domain.event;

import java.sql.Date;
import java.util.UUID;

public record EventDTO(UUID id, String name, String hour, byte duration, java.sql.Date date, String description, int capacity) {

}
