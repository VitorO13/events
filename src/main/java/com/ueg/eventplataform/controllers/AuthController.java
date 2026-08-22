package com.ueg.eventplataform.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.validation.Valid;
import com.ueg.eventplataform.domain.users.AuthenticationDTO;
import com.ueg.eventplataform.domain.users.RegisterDTO;
import com.ueg.eventplataform.domain.users.User;



@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;

    @PostMapping("/login")
    public ResponseEntity login (@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = authenticationManager.authenticate(usernamePassword);
      return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity register (@RequestBody @Valid RegisterDTO data) {
        if(this.repository.findbyEmail(data.email()) != null)return ResponseEntity.badRequest().build();
           
            String hashedPassword = new BCryptPasswordEncoder().encode(data.password());
            User newUser = new User(data.email(), hashedPassword, data.role());
            this.repository.save(newUser);
            return ResponseEntity.ok().build();
        
    }

}
