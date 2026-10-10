package com.ueg.eventplataform.service;

import com.ueg.eventplataform.domain.registration.Registration;
import com.ueg.eventplataform.domain.users.User;
import com.ueg.eventplataform.domain.users.UserRole;
import com.ueg.eventplataform.repositories.RegistrationRepository;
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
class RegistrationServiceTest {
    @Mock RegistrationRepository repository;
    @Mock QRCodeService qrCodeService;
    @InjectMocks RegistrationService service;

    @Test
    void blocksQRCodeForAnotherUser() {
        User owner = new User("Owner", "owner@example.com", "hash", UserRole.USER);
        owner.setId(UUID.randomUUID());
        User requester = new User("Other", "other@example.com", "hash", UserRole.USER);
        requester.setId(UUID.randomUUID());
        Registration registration = new Registration();
        registration.setUser(owner);
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(registration));
        var error = assertThrows(ResponseStatusException.class, () -> service.getQRCode(id, requester));
        assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
        verifyNoInteractions(qrCodeService);
    }

    @Test
    void rejectsRepeatedCheckIn() {
        UUID id = UUID.randomUUID();
        when(repository.checkInIfAbsent(id)).thenReturn(0);
        when(repository.existsById(id)).thenReturn(true);
        var error = assertThrows(ResponseStatusException.class, () -> service.checkIn(id));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
    }

    @Test
    void reportsMissingRegistration() {
        UUID id = UUID.randomUUID();
        var error = assertThrows(ResponseStatusException.class, () -> service.checkIn(id));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    void completesCheckIn() {
        UUID id = UUID.randomUUID();
        when(repository.checkInIfAbsent(id)).thenReturn(1);
        assertDoesNotThrow(() -> service.checkIn(id));
        verify(repository, never()).existsById(any());
    }
}
