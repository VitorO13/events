package com.ueg.eventplataform.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.ueg.eventplataform.domain.users.UserResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import com.ueg.eventplataform.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.ueg.eventplataform.domain.infra_security.TokenService;
import com.ueg.eventplataform.domain.users.AuthenticationDTO;
import com.ueg.eventplataform.domain.users.LoginRespDTO;
import com.ueg.eventplataform.domain.users.RegisterDTO;
import com.ueg.eventplataform.domain.users.User;
import com.ueg.eventplataform.domain.users.UserRole;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    AuthController(UserRepository userRepository, AuthenticationManager authenticationManager,
            TokenService tokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        return loginForClient(data, true);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if (this.userRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }
        String hashedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.name(), data.email(), hashedPassword, UserRole.USER);
        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole()));
    }

    @PostMapping("/participant/login")
    public ResponseEntity<LoginRespDTO> participantLogin(@RequestBody @Valid AuthenticationDTO data) {
        return loginForClient(data, false);
    }

    @PostMapping("/desktop/login")
    public ResponseEntity<LoginRespDTO> desktopLogin(@RequestBody @Valid AuthenticationDTO data) {
        return loginForClient(data, true);
    }

    private ResponseEntity<LoginRespDTO> loginForClient(AuthenticationDTO data, boolean desktop) {
        org.springframework.security.core.Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(data.email(), data.password()));
        } catch (org.springframework.security.core.AuthenticationException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos.");
        }
        User user = (User) authentication.getPrincipal();
        boolean staff = user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.HOST;
        if (desktop != staff) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    desktop ? "Acesso desktop exclusivo para administradores e organizadores."
                            : "Use o aplicativo desktop para acessar sua conta administrativa.");
        }
        return ResponseEntity.ok(new LoginRespDTO(tokenService.generateToken(user)));
    }

}
