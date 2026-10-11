package com.ueg.eventplataform.domain.users;

public record RegisterDTO(
                @jakarta.validation.constraints.NotBlank String name,
                @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Email String email,
                @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(min = 8, max = 72) String password,
                UserRole role) {

}
