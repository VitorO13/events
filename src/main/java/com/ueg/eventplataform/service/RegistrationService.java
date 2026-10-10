package com.ueg.eventplataform.service;

import com.ueg.eventplataform.domain.registration.Registration;
import com.ueg.eventplataform.domain.users.User;
import com.ueg.eventplataform.domain.users.UserRole;
import com.ueg.eventplataform.repositories.RegistrationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final QRCodeService qrCodeService;

    public RegistrationService(RegistrationRepository registrationRepository, QRCodeService qrCodeService) {
        this.registrationRepository = registrationRepository;
        this.qrCodeService = qrCodeService;
    }

    @Transactional(readOnly = true)
    public String getQRCode(UUID id, User requester) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscrição não encontrada."));
        if (!registration.getUser().getId().equals(requester.getId()) && requester.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão para acessar esta inscrição.");
        }
        return qrCodeService.generateQRCodeBase64(registration.getId().toString());
    }

    @Transactional
    public void checkIn(UUID id) {
        if (registrationRepository.checkInIfAbsent(id) == 0) {
            if (!registrationRepository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscrição não encontrada.");
            }
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Check-in já realizado anteriormente.");
        }
    }
}
