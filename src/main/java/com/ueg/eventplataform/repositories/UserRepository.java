package com.ueg.eventplataform.repositories;

import com.ueg.eventplataform.domain.users.User;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;


public interface UserRepository extends JpaRepository<User, UUID> {

    UserDetails findByEmail(String email);
}
