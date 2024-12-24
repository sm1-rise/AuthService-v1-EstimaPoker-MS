package br.com.samuel.martins.AuthService_v1.controller;

import br.com.samuel.martins.AuthService_v1.model.dto.login.LoginResponseDto;
import br.com.samuel.martins.AuthService_v1.model.dto.registerUser.RequestRegisterUserDto;
import br.com.samuel.martins.AuthService_v1.model.dto.registerUser.ResponseDto;
import br.com.samuel.martins.AuthService_v1.model.dto.login.UserRequestDto;
import br.com.samuel.martins.AuthService_v1.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity <LoginResponseDto> login(@RequestBody UserRequestDto body) {
        return ResponseEntity.ok(service.login(body));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody RequestRegisterUserDto body) throws JsonProcessingException {
        return ResponseEntity.status(201).body(service.registerUser(body));
    }
}
