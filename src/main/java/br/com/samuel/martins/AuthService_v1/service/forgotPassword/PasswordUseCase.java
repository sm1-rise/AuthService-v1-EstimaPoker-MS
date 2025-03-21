package br.com.samuel.martins.AuthService_v1.service.forgotPassword;

import br.com.samuel.martins.AuthService_v1.model.dto.reset.EmailRequest;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.NewPasswordDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.ResponseResetPasswordDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.security.NoSuchAlgorithmException;

public interface PasswordUseCase {
    ResponseResetPasswordDto changePassword(NewPasswordDto request) throws JsonProcessingException;
    ResponseResetPasswordDto resetPassword (NewPasswordDto request);
    ResponseResetPasswordDto resetPasswordEmail (EmailRequest request) throws NoSuchAlgorithmException, JsonProcessingException;
}
