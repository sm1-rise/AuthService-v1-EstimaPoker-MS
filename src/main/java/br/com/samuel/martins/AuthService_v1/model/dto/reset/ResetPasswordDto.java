package br.com.samuel.martins.AuthService_v1.model.dto.reset;

import lombok.Builder;

@Builder
public record ResetPasswordDto (String email, String phoneNumbe) {
}
