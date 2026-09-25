package com.ueg.eventplataform.domain.users;

import jakarta.validation.constraints.NotNull;

public record UpdateRoleDTO(@NotNull UserRole role) {

}
