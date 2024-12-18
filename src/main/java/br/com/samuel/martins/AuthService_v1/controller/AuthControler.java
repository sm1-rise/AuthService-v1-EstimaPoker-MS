package br.com.samuel.martins.AuthService_v1.controller;

import br.com.samuel.martins.AuthService_v1.controller.dto.RequestRegisterUserDto;
import br.com.samuel.martins.AuthService_v1.controller.dto.ResponseDto;
import br.com.samuel.martins.AuthService_v1.controller.dto.UserRequestDto;
import br.com.samuel.martins.AuthService_v1.infra.security.TokenService;
import br.com.samuel.martins.AuthService_v1.model.User;
import br.com.samuel.martins.AuthService_v1.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthControler {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthControler(UserRepository repository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserRequestDto body) {
        var user = repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User NOT FOUND"));
        if(passwordEncoder.matches(user.getPasswordHash(), body.password())){
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDto(user.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity login(@RequestBody RequestRegisterUserDto body) {

        Optional<User> user = repository.findByEmail(body.email());
        if(user.isEmpty()) {
            User newUser = new User();
            newUser.setPasswordHash(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setUsername(body.username());
            newUser.setCreatedAt(LocalDateTime.now());
            repository.save(newUser);

            String token = tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDto(newUser.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
