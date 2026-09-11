package com.ueg.eventplataform.domain.users;

public record RegisterDTO(String name, String email, String password, UserRole role) {

}
