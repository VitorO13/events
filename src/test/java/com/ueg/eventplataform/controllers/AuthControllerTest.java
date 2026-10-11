package com.ueg.eventplataform.controllers;

import com.ueg.eventplataform.domain.infra_security.TokenService;
import com.ueg.eventplataform.domain.users.*;
import com.ueg.eventplataform.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock AuthenticationManager manager;
    @Mock UserRepository repository;
    @Mock TokenService tokenService;
    @InjectMocks AuthController controller;

    private void authenticate(UserRole role) {
        User user = new User("Test", "test@example.com", "hash", role);
        when(manager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    @Test void participantCannotUseDesktopLogin() {
        authenticate(UserRole.USER);
        var error = assertThrows(ResponseStatusException.class,
                () -> controller.login(new AuthenticationDTO("test@example.com", "password")));
        assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
        verifyNoInteractions(tokenService);
    }

    @Test void adminCannotUseParticipantLogin() {
        authenticate(UserRole.ADMIN);
        var error = assertThrows(ResponseStatusException.class,
                () -> controller.participantLogin(new AuthenticationDTO("test@example.com", "password")));
        assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
        verifyNoInteractions(tokenService);
    }

    @Test void hostCanUseDesktopLogin() {
        authenticate(UserRole.HOST);
        when(tokenService.generateToken(any())).thenReturn("token");
        assertEquals("token", controller.desktopLogin(new AuthenticationDTO("test@example.com", "password")).getBody().token());
    }

    @Test void participantCanUseWebLogin() {
        authenticate(UserRole.USER);
        when(tokenService.generateToken(any())).thenReturn("token");
        assertEquals("token", controller.participantLogin(new AuthenticationDTO("test@example.com", "password")).getBody().token());
    }

    @Test void publicRegistrationCannotCreateAdmin() {
        controller.register(new RegisterDTO("Test", "test@example.com", "password123", UserRole.ADMIN));
        verify(repository).save(argThat(user -> user.getRole() == UserRole.USER && !user.getPassword().equals("password123")));
    }
}
