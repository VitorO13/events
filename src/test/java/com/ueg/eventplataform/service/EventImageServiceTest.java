package com.ueg.eventplataform.service;

import com.ueg.eventplataform.domain.event.Event;
import com.ueg.eventplataform.repositories.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;
import java.util.Optional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventImageServiceTest {
    @Test void rejectsNonImageFile() {
        EventRepository repository = mock(EventRepository.class);
        var service = new EventImageService(repository);
        assertThrows(ResponseStatusException.class, () -> service.upload(UUID.randomUUID(),
                new MockMultipartFile("file", "fake.png", "image/png", "not an image".getBytes())));
        verifyNoInteractions(repository);
    }

    @Test void storesValidImageAndDeterminesActualType() throws Exception {
        UUID id = UUID.randomUUID();
        EventRepository repository = mock(EventRepository.class);
        Event event = new Event(); event.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(event));
        var output = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB), "png", output);
        var service = new EventImageService(repository);
        service.upload(id, new MockMultipartFile("file", "photo.jpg", "image/jpeg", output.toByteArray()));
        assertEquals("image/png", event.getImageContentType());
        assertArrayEquals(output.toByteArray(), service.get(id).bytes());
        assertEquals("/events/" + id + "/image", event.getImageUrl());
        verify(repository).save(event);
    }

    @Test void missingImageReturns404() {
        UUID id = UUID.randomUUID();
        EventRepository repository = mock(EventRepository.class);
        when(repository.findById(id)).thenReturn(Optional.of(new Event()));
        var error = assertThrows(ResponseStatusException.class, () -> new EventImageService(repository).get(id));
        assertEquals(404, error.getStatusCode().value());
    }
}
