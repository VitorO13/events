package com.ueg.eventplataform.controllers;

import com.ueg.eventplataform.domain.registration.RegistrationResponseDTO;
import com.ueg.eventplataform.domain.users.User;
import com.ueg.eventplataform.service.RegistrationService;
import com.ueg.eventplataform.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserService userService;

    // Injeção de dependências no construtor
    public RegistrationController(RegistrationService registrationService, UserService userService) {
        this.registrationService = registrationService;
        this.userService = userService;
    }

    @PostMapping("/events/{eventId}")
    public ResponseEntity<RegistrationResponseDTO> register(@PathVariable UUID eventId,
            @AuthenticationPrincipal User user) {
        var registration = userService.registerUserToEvent(user.getId(), eventId);
        return ResponseEntity.status(201).body(RegistrationResponseDTO.from(registration));
    }

    @GetMapping("/me")
    public ResponseEntity<java.util.List<RegistrationResponseDTO>> myRegistrations(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(registrationService.listForUser(user.getId()));
    }

    // O QR Code fica acessível ao titular da inscrição e ao administrador.
    @GetMapping("/{id}/qrcode")
    public ResponseEntity<String> getRegistrationQRCode(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        String qrCode = this.registrationService.getQRCode(id, user);
        return ResponseEntity.ok(qrCode);
    }

    @PatchMapping("/{id}/checkin")
    public ResponseEntity<String> performCheckIn(@PathVariable UUID id) {
        this.registrationService.checkIn(id);

        return ResponseEntity.ok("Check-in realizado com sucesso! Entrada autorizada.");
    }
}
