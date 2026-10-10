package com.ueg.eventplataform.service;

import com.ueg.eventplataform.domain.event.Event;
import com.ueg.eventplataform.domain.registration.Registration;
import com.ueg.eventplataform.domain.users.User;
import com.ueg.eventplataform.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock UserRepository users;
    @Mock EventRepository events;
    @Mock RegistrationRepository registrations;
    @InjectMocks UserService service;

    private void availableEvent(UUID userId, UUID eventId) {
        when(users.findById(userId)).thenReturn(Optional.of(new User()));
        Event event = new Event();
        event.setCapacity(1);
        when(events.findByIdForUpdate(eventId)).thenReturn(Optional.of(event));
    }

    @Test
    void rejectsDuplicateRegistration() {
        UUID userId = UUID.randomUUID(), eventId = UUID.randomUUID();
        availableEvent(userId, eventId);
        when(registrations.existsByUserIdAndEventId(userId, eventId)).thenReturn(true);
        var error = assertThrows(ResponseStatusException.class, () -> service.registerUserToEvent(userId, eventId));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(registrations, never()).save(any());
    }

    @Test
    void rejectsFullEvent() {
        UUID userId = UUID.randomUUID(), eventId = UUID.randomUUID();
        availableEvent(userId, eventId);
        when(registrations.countByEventId(eventId)).thenReturn(1L);
        var error = assertThrows(ResponseStatusException.class, () -> service.registerUserToEvent(userId, eventId));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(registrations, never()).save(any());
    }

    @Test
    void createsRegistrationWithDateAndWithoutPresence() {
        UUID userId = UUID.randomUUID(), eventId = UUID.randomUUID();
        availableEvent(userId, eventId);
        when(registrations.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Registration result = service.registerUserToEvent(userId, eventId);
        assertNotNull(result.getUser());
        assertNotNull(result.getEvent());
        assertNotNull(result.getRegistrationDate());
        assertFalse(result.isPresent());
    }

    @Test
    void preventsDeletingUserWithRegistrations() {
        UUID id = UUID.randomUUID();
        when(users.existsById(id)).thenReturn(true);
        when(registrations.existsByUserId(id)).thenReturn(true);
        var error = assertThrows(ResponseStatusException.class, () -> service.deleteUser(id));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(users, never()).deleteById(any());
    }
}
