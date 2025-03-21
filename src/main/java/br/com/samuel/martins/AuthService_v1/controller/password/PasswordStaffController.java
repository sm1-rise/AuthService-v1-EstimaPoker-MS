package br.com.samuel.martins.AuthService_v1.controller.password;

import br.com.samuel.martins.AuthService_v1.model.dto.reset.EmailRequest;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.NewPasswordDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.ResponseResetPasswordDto;
import br.com.samuel.martins.AuthService_v1.service.forgotPassword.PasswordStaffService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
public class PasswordStaffController {

    private final PasswordStaffService service;

    public PasswordStaffController(PasswordStaffService service) {
        this.service = service;
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<ResponseResetPasswordDto> changePassword(@RequestBody NewPasswordDto newPasswordDto) throws JsonProcessingException {
        return ResponseEntity.ok(service.changePassword(newPasswordDto));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Void> reset(@RequestBody EmailRequest request) {
        return ResponseEntity.noContent().build();
    }

}
