package com.ueg.eventplataform.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.ueg.eventplataform.domain.users.UpdateRoleDTO;
import com.ueg.eventplataform.domain.users.UserRole;
import com.ueg.eventplataform.repositories.UserRepository;
import com.ueg.eventplataform.service.UserService;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserControllerTest {

  @Test
  void adminRoleMustBeAssignedManuallyInDatabase() {
    UserRepository repository = mock(UserRepository.class);
    UserController controller = new UserController(
      repository,
      mock(UserService.class)
    );
    assertEquals(
      403,
      controller
        .updateUserRole(UUID.randomUUID(), new UpdateRoleDTO(UserRole.ADMIN))
        .getStatusCode()
        .value()
    );
    verifyNoInteractions(repository);
  }
}
