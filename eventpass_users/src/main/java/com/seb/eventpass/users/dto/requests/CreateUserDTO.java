package com.seb.eventpass.users.dto.requests;

public record CreateUserDTO(String firstname, String lastname, String role, String email, String password) {
}
