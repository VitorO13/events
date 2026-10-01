package com.ueg.eventplataform.controllers;

import com.ueg.eventplataform.domain.registration.Registration;
import com.ueg.eventplataform.repositories.RegistrationRepository;
import com.ueg.eventplataform.service.QRCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    private final QRCodeService qrCodeService;
    private final RegistrationRepository registrationRepository;

    // Injeção de dependências no construtor
    public RegistrationController(QRCodeService qrCodeService, RegistrationRepository registrationRepository) {
        this.qrCodeService = qrCodeService;
        this.registrationRepository = registrationRepository;
    }

    // Rota pública (ou acessível apenas pelo próprio aluno) para gerar o QR Code
    @GetMapping("/{id}/qrcode")
    public ResponseEntity<String> getRegistrationQRCode(@PathVariable UUID id) {
        String qrCode = this.qrCodeService.generateQRCodeBase64(id.toString());
        return ResponseEntity.ok(qrCode);
    }

  
    @PatchMapping("/{id}/checkin")
    public ResponseEntity<String> performCheckIn(@PathVariable UUID id) {
        Registration registration = this.registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada."));

        if (registration.isPresent()) {
            return ResponseEntity.badRequest().body("Check-in já realizado anteriormente!");
        }

        registration.setPresent(true);
        this.registrationRepository.save(registration);

        return ResponseEntity.ok("Check-in realizado com sucesso! Entrada autorizada.");
    }
}