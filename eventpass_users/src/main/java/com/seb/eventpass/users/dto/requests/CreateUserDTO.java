package com.seb.eventpass.users.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(
        @NotBlank(message = "Falta el primer nombre.")
        String firstname,
        @NotBlank(message = "Falta el apellido.")
        String lastname,

        @NotBlank(message = "Falta un correo electronico.")
        @Email(message = "Ingrese un correo electronico valido.")
        String email,

        @NotBlank(message = "Falta una contraseña.")
        String password) {
}
