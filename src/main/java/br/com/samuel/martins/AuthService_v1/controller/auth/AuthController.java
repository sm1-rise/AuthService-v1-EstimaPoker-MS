package br.com.samuel.martins.AuthService_v1.controller.auth;

import br.com.samuel.martins.AuthService_v1.model.dto.login.LoginResponseDto;
import br.com.samuel.martins.AuthService_v1.model.dto.registerUser.RequestRegisterUserDto;
import br.com.samuel.martins.AuthService_v1.model.dto.registerUser.ResponseDto;
import br.com.samuel.martins.AuthService_v1.model.dto.login.UserRequestDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.EmailRequest;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.NewPasswordDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.ResponseResetPasswordDto;
import br.com.samuel.martins.AuthService_v1.service.auth.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
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
