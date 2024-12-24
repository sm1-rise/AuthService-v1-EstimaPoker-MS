package br.com.samuel.martins.AuthService_v1.model.dto.reset;

public record NewPasswordDto(String email, String oldPassword, String newPassword){
}
