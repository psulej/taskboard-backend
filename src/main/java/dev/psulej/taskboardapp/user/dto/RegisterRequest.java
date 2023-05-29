package dev.psulej.taskboardapp.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegisterRequest(
        @NotBlank String login,
        @NotBlank String password,
        @NotBlank String name,
        @Email String email
) {
}
