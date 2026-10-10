package com.ueg.eventplataform.domain.users;

import java.util.UUID;

public record UserResponseDTO(UUID id, String name, String email, UserRole role) {
}
