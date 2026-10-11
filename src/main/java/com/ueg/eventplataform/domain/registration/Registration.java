package com.ueg.eventplataform.domain.registration;

import com.ueg.eventplataform.domain.event.Event;
import com.ueg.eventplataform.domain.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "registrations")
@Table(name = "registrations")
@Getter
@Setter
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private LocalDateTime registrationDate;
    private boolean isPresent = false;
}