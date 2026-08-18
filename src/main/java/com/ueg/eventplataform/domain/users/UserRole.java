package com.ueg.eventplataform.domain.users;

public enum UserRole {
    ADMIN("admin"),
    USER("user"),
    HOST("host");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
