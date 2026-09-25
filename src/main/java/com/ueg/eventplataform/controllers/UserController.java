package com.ueg.eventplataform.controllers;

import com.ueg.eventplataform.domain.users.UpdateRoleDTO;
import com.ueg.eventplataform.domain.users.User;
import com.ueg.eventplataform.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = this.userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    
    @PutMapping("/{id}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable UUID id, @RequestBody @Valid UpdateRoleDTO data) {
        User user = this.userRepository.findById(id).orElse(null);
        
        if (user == null) {
            return ResponseEntity.notFound().build(); 
        }

        user.setRole(data.role());

        this.userRepository.save(user);

        return ResponseEntity.noContent().build();
    
    }

     public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        
        if (!this.userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        this.userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
        }
    
}