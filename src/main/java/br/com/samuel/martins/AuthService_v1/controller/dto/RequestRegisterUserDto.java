package br.com.samuel.martins.AuthService_v1.controller.dto;

public record RequestRegisterUserDto(String username,
                                     String email,
                                     String password) {}
