package br.com.samuel.martins.AuthService_v1.service.auth;

import br.com.samuel.martins.AuthService_v1.model.dto.login.LoginResponseDto;
import br.com.samuel.martins.AuthService_v1.model.dto.registerUser.RequestRegisterUserDto;
import br.com.samuel.martins.AuthService_v1.model.dto.registerUser.ResponseDto;
import br.com.samuel.martins.AuthService_v1.model.dto.login.UserRequestDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.EmailRequest;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.NewPasswordDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.ResponseResetPasswordDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.security.NoSuchAlgorithmException;

public interface AuthServiceUseCase {
    ResponseDto registerUser(RequestRegisterUserDto request) throws JsonProcessingException;
    LoginResponseDto login (UserRequestDto request);

}
