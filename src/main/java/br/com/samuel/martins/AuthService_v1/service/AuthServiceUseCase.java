package br.com.samuel.martins.AuthService_v1.service;

import br.com.samuel.martins.AuthService_v1.model.dto.login.LoginResponseDto;
import br.com.samuel.martins.AuthService_v1.model.dto.registerUser.RequestRegisterUserDto;
import br.com.samuel.martins.AuthService_v1.model.dto.registerUser.ResponseDto;
import br.com.samuel.martins.AuthService_v1.model.dto.login.UserRequestDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.NewPasswordDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.ResetPasswordDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.ResponseResetPasswordDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AuthServiceUseCase {
    ResponseDto registerUser(RequestRegisterUserDto request) throws JsonProcessingException;
    LoginResponseDto login (UserRequestDto request);
    ResponseResetPasswordDto changePassword(NewPasswordDto request);
    void sendPINtoEmail(ResetPasswordDto request);
    void vefifyPIN (ResetPasswordDto request);
    ResponseResetPasswordDto resetPassword (NewPasswordDto request);

}
