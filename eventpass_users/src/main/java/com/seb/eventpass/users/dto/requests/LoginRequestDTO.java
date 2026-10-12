package com.seb.eventpass.users.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Email(message = "Ingrese un correo electronico valido.")
        String email,

        @NotBlank(message = "Falta una contraseña.")
        String password) {
}
