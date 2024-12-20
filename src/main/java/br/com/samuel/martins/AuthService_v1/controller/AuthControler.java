package br.com.samuel.martins.AuthService_v1.controller;

import br.com.samuel.martins.AuthService_v1.controller.dto.LoginResponseDto;
import br.com.samuel.martins.AuthService_v1.controller.dto.RequestRegisterUserDto;
import br.com.samuel.martins.AuthService_v1.controller.dto.ResponseDto;
import br.com.samuel.martins.AuthService_v1.controller.dto.UserRequestDto;
import br.com.samuel.martins.AuthService_v1.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthControler {

    private final AuthService service;

    public AuthControler( AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity <LoginResponseDto> login(@RequestBody UserRequestDto body) {
        return ResponseEntity.ok(service.login(body));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody RequestRegisterUserDto body) {
        return ResponseEntity.status(201).body(service.registerUser(body));
    }
}
