package com.ueg.eventplataform.controllers;

import com.ueg.eventplataform.service.EventImageService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@RestController
@RequestMapping("/events/{id}/image")
public class EventImageController {
    private final EventImageService service;

    public EventImageController(EventImageService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> upload(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        service.upload(id, file);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<byte[]> get(@PathVariable UUID id) {
        var image = service.get(id);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.contentType()))
                .header("X-Content-Type-Options", "nosniff").body(image.bytes());
    }
}
