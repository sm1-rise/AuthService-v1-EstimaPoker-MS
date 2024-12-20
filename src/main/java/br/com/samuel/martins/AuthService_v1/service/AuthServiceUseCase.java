package br.com.samuel.martins.AuthService_v1.service;

import br.com.samuel.martins.AuthService_v1.controller.dto.LoginResponseDto;
import br.com.samuel.martins.AuthService_v1.controller.dto.RequestRegisterUserDto;
import br.com.samuel.martins.AuthService_v1.controller.dto.ResponseDto;
import br.com.samuel.martins.AuthService_v1.controller.dto.UserRequestDto;

public interface AuthServiceUseCase {
    ResponseDto registerUser(RequestRegisterUserDto request);
    LoginResponseDto login (UserRequestDto request);
}
