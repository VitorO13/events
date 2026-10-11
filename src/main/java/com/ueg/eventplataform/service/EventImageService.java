package com.ueg.eventplataform.service;

import com.ueg.eventplataform.repositories.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class EventImageService {
    private final EventRepository repository;

    public EventImageService(EventRepository repository) {
        this.repository = repository;
    }

    public record ImageData(byte[] bytes, String contentType) {
    }

    @Transactional
    public void upload(UUID id, MultipartFile file) {
        if (file.isEmpty() || file.getSize() > 5 * 1024 * 1024) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie uma imagem de até 5 MB.");
        }
        byte[] bytes;
        String mime;
        try {
            bytes = file.getBytes();
            try (var input = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
                var readers = ImageIO.getImageReaders(input);
                if (!readers.hasNext())
                    throw new IOException("Imagem inválida");
                var reader = readers.next();
                try {
                    reader.setInput(input);
                    String format = reader.getFormatName();
                    if (!format.equalsIgnoreCase("png") && !format.equalsIgnoreCase("jpeg"))
                        throw new IOException("Formato inválido");
                    if ((long) reader.getWidth(0) * reader.getHeight(0) > 20_000_000)
                        throw new IOException("Imagem muito grande");
                    reader.read(0);
                    mime = format.equalsIgnoreCase("png") ? "image/png" : "image/jpeg";
                } finally {
                    reader.dispose();
                }
            }
        } catch (IOException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arquivo PNG ou JPEG inválido.");
        }
        var event = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));
        event.setImage(bytes);
        event.setImageContentType(mime);
        repository.save(event);
    }

    @Transactional(readOnly = true)
    public ImageData get(UUID id) {
        var event = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (event.getImage() == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento sem imagem.");
        return new ImageData(event.getImage(), event.getImageContentType());
    }
}
